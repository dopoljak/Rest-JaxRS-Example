/**
* Check if session is still active on server side 
**/
function checkSession() 
{
    // check if session is active, if isn't load LOGIN popup
    Ext.Ajax.request
    ({
        url: WS_URL + 'sessions',
        method: 'GET',
        scope: this,
        success: function(response, opts) 
        {
            userSession = Ext.JSON.decode(response.responseText);
            mainStatus.setCurrentUser(userSession.user.username);
        },
        failure: function(response) 
        {
            //CREATE LOGIN POPUP FORM
            showLoginForm();
        }
    });
}

/**
* kill users session
**/
function logout() 
{
    function clearAll()
    {
        userSession = null;

        // close all TABs
        if (mainCenterTabs !== null) 
        {
            mainCenterTabs.items.each(function(item) 
            {
                mainCenterTabs.remove(item);
            });
        }

        // show login window
        showLoginForm();

        // set status to null
        if (mainStatus !== null) {
            mainStatus.setNotLoggedIn();
        }
    };
    
    //create an AJAX request
    Ext.Ajax.request
    ({
        url: WS_URL + 'sessions',
        method: 'DELETE',
        scope: this,
        success: function(response, opts) 
        {
            userSession = Ext.JSON.decode(response.responseText);

            Ext.example.msg('Sucessfully logged out', '{0}, {1}, {2}', userSession.user.username, userSession.user.firstname, userSession.user.lastname);
            clearAll();
        },
        failure: function(response) 
        {
            failureException(response);
            clearAll();
        }
    });
}


/**
* Show login form
**/            
function showLoginForm() 
{
    function login_post() 
    {
        var form = Ext.getCmp('loginForm').getForm();
        if (form.isValid()) 
        {
            //create an AJAX request
            Ext.Ajax.request({
                url: WS_URL + 'sessions',
                method: 'POST',
                form: 'form',
                params: form.getValues(),
                scope: this,
                success: function(response, opts) 
                {
                    userSession = Ext.JSON.decode(response.responseText);

                    Ext.example.msg('Successfully logged', '{0}, {1} {2}', userSession.user.username, userSession.user.firstname, userSession.user.lastname);

                    // close
                    form.reset();
                    //form.close();
                    Ext.getCmp('main_login_window').close();

                    // create
                    mainStatus.setCurrentUser(userSession.user.username);
                },
                failure: failureException
            });
        }
    }

    if (Ext.getCmp('loginForm') == null)
    {
        var form = Ext.widget('form', 
        {
            id: 'loginForm',
            frame: true,
            border: false,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            bodyPadding: 10,
            items: 
            [{
                afterLabelTextTpl: requiredStyle,
                allowBlank: false,
                fieldLabel: 'Username',
                xtype: 'textfield',
                name: 'username'
            },{
                afterLabelTextTpl: requiredStyle,
                allowBlank: false,
                inputType: 'password',
                fieldLabel: 'Password',
                xtype: 'textfield',
                name: 'password',
                enableKeyEvents: true,
                listeners: {
                    specialkey: function(field, e) {
                        if (e.getKey() == e.ENTER) {
                            login_post();
                        }
                    }
                }
            }],
            buttons: [{
                id: 'loginButton',
                text: 'Login',
                handler: function() {
                    login_post();
                }
            }]
        });

        var win = Ext.widget('window', 
        {
            id: 'main_login_window',
            title: 'Login',
            closable: false,
            width: 300,
            height: 140,
            layout: 'fit',
            resizable: true,
            modal: true,
            items: form,
            onEsc: function() 
            {
            }
        });
        win.show();
    }
}