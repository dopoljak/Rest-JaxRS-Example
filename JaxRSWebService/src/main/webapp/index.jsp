<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page session="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>


<html>

    <head>
        <title>.: EXAMPLE :.</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        
        <!-- Configuration -->
        <script type="text/javascript">
            // GLOBAL JaxRS REST API Endpoint
            var WS_URL = '/JaxRSWebService/api/';
	</script>
        
        <!-- CORE EXTJS JS/CSS -->
        <link   type="text/css" rel="stylesheet" href="Content/resources/css/ext-all.css" />
        <link   type="text/css" rel="stylesheet" href="Content/resources/css2/statusbar.css" />
        <link   type="text/css" rel="stylesheet" href="Content/resources/css2/example.css" />
        <script type="text/javascript" src="Content/core/ext-all.js"></script>
        <script type="text/javascript" src="Content/core/StatusBar.js"></script>
        <script type="text/javascript" src="Content/core/examples.js"></script>

        <!-- COMMON CSS -->
        <link   type="text/css" rel="stylesheet" href="Content/resources/css2/icons.css" />
        <link   type="text/css" rel="stylesheet" href="Content/resources/css2/header.css" />

        <!-- COMMON JS -->
        <script type="text/javascript" src="Content/commons/exception.js"></script>
        <script type="text/javascript" src="Content/commons/newStatusBar.js"></script>
        
        <!-- MODELS -->
        <script type="text/javascript" src="Content/models/user.js"></script>
        
        <!-- MIXED VIEW + CONTROLER -->
        <script type="text/javascript" src="Content/forms/loginForm.js"></script>
        <script type="text/javascript" src="Content/forms/usersForm.js"></script>
        
        
        <!-- APP ENTRY POINT & login form -->
        <script type="text/javascript">

            // GLOBAL VARIABLES
            var mainCenterTabs    = null;
            var mainStatus        = null;
            var userSession       = null;
            var mainHeaderToolbar = null;

            Ext.onReady(function() 
            {
                Ext.require(['*']);
                Ext.tip.QuickTipManager.init();
                                
                // TABS PANEL
                mainCenterTabs = Ext.widget('tabpanel', 
             	{
                    bodyStyle	: 'background:#f1f1f1',
                    plain	: true,
                    region	: 'center',
                    defaults: {
                        //autoScroll: true, // ako je autoscroll ukljucen onda se mora izgleda napraviti autorefresh nakon sto se neki panel unutar njega colapsa
                    },
                    listeners: 
                    {
                        'tabchange': function (tabPanel, tab) 
                        {
                            // bug fix for IE9 : last tab is not showing up after opening it
                            if (Ext.isIE9)
                            {
                                Ext.MessageBox.show({ title : '', msg : '' });
                                Ext.MessageBox.hide(); 
                            }
                        }
                    }
                });

                // CENTER TABS CONTAINER
                var mainCenter = 
                {
                    layout	: 'border',
                    region	: 'center',                       	
                    cls		: 'empty',
                    items	: mainCenterTabs
		};

                // HEADER
    		mainHeaderToolbar = Ext.create('Ext.toolbar.Toolbar', 
                {
                    style: {
                        border  : 0,
                    	padding : 0
                    },
                    height	: 32,
                    region	: 'north',
                    items	:
                    [
                        {
                            id		: 'app-header',
                            xtype	: 'box',
                            html	: 'Example ExtJS + JaxRS'
			},
			'->',
                        {
                            text	: 'Main menu',
                            iconCls	: 'icon-menu',                            
                            menu: 
                            {
                                xtype: 'menu',
                         	plain: true,
                         	items: 
                                {
                                    //title: 'User options',
                                    xtype: 'buttongroup',                                    
                                    columns: 2,
                                    defaults: 
                                    {
                                        xtype	  : 'button',                         		   	
                         		scale	  : 'large',
                         		iconAlign : 'left'
                                    },
                                    items: 
                                    [{
                                        iconCls	: 'icon-group',
					colspan	: 2,
					text	: 'Users',
					scale	: 'small',
					width	: 130,
                                        handler	: function() 
					{
                                            var dash = Ext.getCmp('tab-users');
                                            if(dash == null) {
                                                showUsersGridTab();
                                            }
                                            else {
                                                mainCenterTabs.setActiveTab(dash);
						Ext.example.msg('Info', '{0}', 'Users tab is allready active!');
                                            }
					}
                                    },{
                                        text	: 'Logout',
					iconCls	: 'icon-disconnect',
					colspan	: 2,						                	
					scale	: 'small',
					width	: 130,
					handler	: function() 
					{
                                            logout();
                                        }
                                    }]
				}
                            }
                         }]
                    });

                // STATUS
                mainStatus = Ext.create('NewStatusbar', {});
    		
                // MAIN VIEWPORT
                var viewport = new Ext.Viewport
                ({
                    layout:'border',
                    items:
                    [
                        mainHeaderToolbar,
                        mainCenter,
                        mainStatus
                    ]
                });
                
                // check session
                checkSession();
                
             });

    	</script>
               
    </head>
    
    <body>
    </body>

</html>

