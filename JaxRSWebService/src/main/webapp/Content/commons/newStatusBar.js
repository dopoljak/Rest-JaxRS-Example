Ext.define('NewStatusbar', 
{
    id: 'basic-statusbar',
    region: 'south',
    extend: 'Ext.ux.statusbar.StatusBar',
    defaultText: 'ExtJS + JaxRS Example',
    height: 20,
    minHeight: 20,
    maxHeight: 20,
    autoClear: 3000,
    loggedUser: '',
    initComponent: function() 
    {
        this.currentUserDisplay = Ext.create('Ext.toolbar.TextItem');
        this.currentUserDisplay.setText('Not logged in!');
        this.timeDisplay = Ext.create('Ext.toolbar.TextItem');
        this.timeDisplay.setText(Ext.Date.format(new Date(), 'Y-m-d H:i:s'));

        Ext.apply(this, 
        {
            items: [
            this.currentUserDisplay,
            {
                xtype: 'tbseparator'
            },
            this.timeDisplay],
            listeners: {
                render: function() {
                    Ext.TaskManager.start({
                        run: function() {
                            this.timeDisplay.setText(Ext.Date.format(new Date(), 'Y-m-d H:i:s'));
                        },
                        interval: 1000,
                        scope: this
                    });
                }
            }
        });

        this.callParent();
    },
    setNotLoggedIn: function()
    {
        this.currentUserDisplay.setText('Not logged in!');
    },
    setCurrentUser: function(username) 
    {
        this.loggedUser = 'Logged as: ' + username;
        this.currentUserDisplay.setText(this.loggedUser);
    },
    startLoad: function(message) 
    {
        if (message !== null) {
            this.showBusy({
                text: message,
                iconCls: "x-status-busy"
            });
        } else {
            this.showBusy();
        }
    },
    endLoad: function() {
        this.clearStatus({
            useDefaults: true
        });
    }
});