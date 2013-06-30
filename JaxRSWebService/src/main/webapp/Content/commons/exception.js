	
var requiredStyle = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';

function failureException(response) 
{
    var title   = response.status + ' - ' + response.statusText;
    var message = response.responseText !== null ? response.responseText : '';
                                
    Ext.MessageBox.show
    ({
        title 	: title,
        msg 	: message,
        buttons : Ext.MessageBox.OK,
        icon 	: Ext.MessageBox.ERROR
    });
};

function restException(proxy, response, options) 
{
    //console.log('------------------------ restException ---------------------------');

    var method = '';

    try {
        method = options.action.toUpperCase();
    }
    catch (err) {
    }

    try 
    {
        if(response.status === 401)
        {
            //TODO: popup & after OK clear sessions & close windows ...
            //console.log('Statuse = 401, redirecting to login page ...');
            //location.reload();
            logout();
        }
        else
        {
            var title   = method + ' : ' + response.status + ' - ' + response.statusText;
            var message = response.responseText !== null ? response.responseText : '';

            // CONFLICT(409) + ConstraintViolationException or DELETE
            if(response.status === 409 && (message.indexOf("org.hibernate.exception.ConstraintViolationException") !== -1 || method === 'DESTROY'))
            {
                message = 'ConstraintViolationException' + ' <br/><br/>(' + message + ')';
            }

            // CONFLICT(409) + CREATE, entity allready exist in database ...
            else if(response.status === 409 && method === 'CREATE')
            {
                message = 'EntityExistsException' + ' <br/><br/>(' + message + ')';
            }
            
            // BAD REQUEST(400) 
            else if(response.status === 400 && message.indexOf("java.lang.IllegalArgumentException") !== -1)
            {
                message = 'IllegalArgumentException' + ' <br/><br/>(' + message + ')';
            }

            Ext.MessageBox.show
            ({
                title 	: title,
                msg 	: message,
                buttons : Ext.MessageBox.OK,
                icon 	: Ext.MessageBox.ERROR
            });
        }
    }
    catch (err) {
        console.log(err);
    }
}

   