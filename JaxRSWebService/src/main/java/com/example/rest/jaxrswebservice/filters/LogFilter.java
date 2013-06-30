package com.example.rest.jaxrswebservice.filters;

import com.example.rest.database.HibernateUtil;
import com.example.rest.jaxrsutils.HexDump;
import com.example.rest.jaxrsutils.StopWatch;
import com.example.rest.jaxrswebservice.commons.UserSession;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse.Status;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dopoljak@gmail.com
 * 
 *         ### LOG FILTER ###
 * 
 *         This LogFilter only changes thread name so output of Log4j can be
 *         nicely formated :) It also outputs request and response (response can
 *         be tricky) in slf4j debug mode
 * 
 */
public class LogFilter implements Filter
{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(LogFilter.class);
    private static final AtomicInteger nextId = new AtomicInteger(0);

    @Override
    public void init(FilterConfig config) throws ServletException
    {
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException
    {
	final StopWatch timer = new StopWatch();
	final Thread curThr = Thread.currentThread();
	final String oldName = curThr.getName();
	curThr.setName(String.format("T_%09d", getNextTransactionId()));

	try
	{
	    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
	    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
	    HttpSession session = httpRequest.getSession(true);
	    UserSession userSession = (UserSession) session.getAttribute(UserSession.USER_SESSION);

	    log.info("Method = {}, Path = {}{}, Query = {}", new Object[] { httpRequest.getMethod(), httpRequest.getServletPath(), httpRequest.getPathInfo(), httpRequest.getQueryString() });
	    log.info("UserSession = {}, SessionID = {}, IP = {}", new Object[] { userSession, session.getId(), httpRequest.getRemoteAddr() } );

	    if (log.isDebugEnabled())
	    {
		HTTPRequestWrapper request = new HTTPRequestWrapper(httpRequest);
		HTTPResponseWrapper response = new HTTPResponseWrapper(httpResponse);

		// LOG REQUEST
		log.debug("--------------------------------------------------------------------------------------------");
		log.debug(" > IP      | {}", request.getRemoteAddr());
		log.debug(" > METHOD  | {}", request.getMethod());
		log.debug(" > PATH    | {}", request.getRequestURI());
		log.debug(" > REQ MAP | {}", getTypesafeRequestMap(request));

		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements())
		{
		    String name = (String) names.nextElement();
		    Enumeration<String> values = request.getHeaders(name);

		    if (values != null)
		    {
			while (values.hasMoreElements())
			{
			    String value = (String) values.nextElement();
			    log.debug(" > HEADER  | {} : {}", name, value);
			}
		    }
		}

		log.debug(" > CONTENT | {}", request.getRequestBody());
		log.debug("--------------------------------------------------------------------------------------------");

		// PROCESS REQUEST & GET RESPONSE
		chain.doFilter(request, response);

		// LOG RESPONSE
		log.debug("--------------------------------------------------------------------------------------------");
		log.debug(" < CONTENT TYPE | {}", response.getContentType());
		log.debug(" < STATUS       | {}.", response.getStatus());

		Collection<String> headers = response.getHeaderNames();
		for (String string : headers)
		{
		    log.debug(" < HEADER       | {} : {}", string, response.getHeader(string));
		}

		// WE NEED TO OVERIDE WRITER ALLSO TO FULLY SUPPORT DUMP OF ALL
		// RESPONSE DATA !!!
		if (response.getContentByte() != null)
		{
		    log.debug(" < CONTENT SIZE | {}", response.getContentByte().length);
		    log.debug(" < CONTENT      | ");

		    if ("image/gif".equalsIgnoreCase(response.getContentType()))
		    {
			log.debug(HexDump.dump(response.getContentByte(), 0, response.getContentByte().length));
		    }
		    else
			log.debug(response.getContentString());
		}

		log.debug("--------------------------------------------------------------------------------------------");

	    }
	    else
	    {
		chain.doFilter(servletRequest, servletResponse);
	    }

	    log.info("JAX-RS returned : code = {}, status = {}", httpResponse.getStatus(), Status.fromStatusCode(httpResponse.getStatus()));
	}
	catch (Throwable e)
	{
	    log.error("LogFilter error: ", e);
	    log.info("Rollinback transaction silently ...");
	    HibernateUtil.rollbackTransactionSilently();
	    
	    /** set response data **/
	    try
	    {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.getOutputStream().println("Something BAD happened to server, try again :)");
		response.getOutputStream().close();
	    }
	    catch (Throwable e2)
	    {
		log.error("Set Error Message: ", e2);
	    }
	}
	finally
	{
	    log.info("Total execution time = {}", timer.getFormatedExecTime());
	    curThr.setName(oldName);
	}
    }

    private static Integer getNextTransactionId()
    {
        return nextId.incrementAndGet();
    }

    public static Integer getCurrentTransactionId()
    {
	return nextId.get();
    }

    private Map<String, String> getTypesafeRequestMap(HttpServletRequest request)
    {
	Map<String, String> typesafeRequestMap = new HashMap<String, String>();
	Enumeration<?> requestParamNames = request.getParameterNames();
	while (requestParamNames.hasMoreElements())
	{
	    String requestParamName = (String) requestParamNames.nextElement();
	    String requestParamValue = request.getParameter(requestParamName);
	    typesafeRequestMap.put(requestParamName, requestParamValue);
	}
	return typesafeRequestMap;
    }

    public class HTTPResponseWrapper extends HttpServletResponseWrapper
    {
	TeeOutputStreamWrapper tee;
	ByteArrayOutputStream bos;
	HttpServletResponse response;

	public HTTPResponseWrapper(HttpServletResponse response)
	{
	    super(response);
	    this.response = response;
	}

	@Override
	public PrintWriter getWriter() throws IOException
	{
	    // WE NEED TO OVERIDE WRITER ALLSO TO FULLY SUPPORT DUMP OF ALL
	    // RESPONSE DATA !!!
	    // System.out.println("--- IMPLEMENT THIS ---");
	    return super.getWriter();
	}

	public byte[] getContentByte()
	{
	    if (bos != null)
		return bos.toByteArray();
	    else
		return null;
	}

	public String getContentString()
	{
	    if (bos != null)
		return bos.toString();
	    else
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException
	{
	    if (tee == null)
	    {
		bos = new ByteArrayOutputStream();
		tee = new TeeOutputStreamWrapper(response.getOutputStream(), bos);
	    }
	    return tee;
	}

	private class TeeOutputStreamWrapper extends ServletOutputStream
	{
	    private final TeeOutputStream targetStream;

	    public TeeOutputStreamWrapper(OutputStream one, OutputStream two)
	    {
		targetStream = new TeeOutputStream(one, two);
	    }

	    public void write(int arg0) throws IOException
	    {
		this.targetStream.write(arg0);
	    }

	    public void flush() throws IOException
	    {
		super.flush();
		this.targetStream.flush();
	    }

	    public void close() throws IOException
	    {
		super.close();
		this.targetStream.close();
	    }
	}
    }

    public class HTTPRequestWrapper extends HttpServletRequestWrapper
    {
	private byte[] buffer;

	public HTTPRequestWrapper(HttpServletRequest request) throws IOException
	{
	    super(request);

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    InputStream is = request.getInputStream();
	    byte buf[] = new byte[1024];
	    int letti;
	    while ((letti = is.read(buf)) > 0)
	    {
		out.write(buf, 0, letti);
	    }

	    this.buffer = out.toByteArray();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
	    ByteArrayInputStream input = new ByteArrayInputStream(buffer);
	    return new ServletInputStreamWrapper(input);
	}

	public String getRequestBody() throws IOException
	{
	    BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
	    String line = null;
	    StringBuilder inputBuffer = new StringBuilder();
	    do
	    {
		line = reader.readLine();
		if (null != line)
		{
		    inputBuffer.append(line.trim());
		}
	    } while (line != null);
	    reader.close();
	    return inputBuffer.toString().trim();
	}

	private class ServletInputStreamWrapper extends ServletInputStream
	{
	    private ByteArrayInputStream bais;

	    public ServletInputStreamWrapper(ByteArrayInputStream bais)
	    {
		this.bais = bais;
	    }

	    @Override
	    public int available()
	    {
		return this.bais.available();
	    }

	    @Override
	    public int read()
	    {
		return this.bais.read();
	    }

	    @Override
	    public int read(byte[] buf, int off, int len)
	    {
		return this.bais.read(buf, off, len);
	    }
	}
    }
}
