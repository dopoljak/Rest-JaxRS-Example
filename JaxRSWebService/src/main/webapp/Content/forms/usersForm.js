function showUsersGridTab()
{    
    var userStore = Ext.create('Ext.data.Store', 
    {
        pageSize  : 25,
	model	  : 'User',
	autoSync  : false,
    	autoLoad  : false
    });
    userStore.load();

    var userGrid = Ext.create('Ext.grid.Panel', 
    {
        store	: userStore,    		
        stateful: true,
        stateId	: 'stateGrid',
        columns	: 
        [
            {
                text     : 'Username',
		flex     : 1,
		sortable : false,
		dataIndex: 'username'
            },{
                text     : 'Firstname',
                flex     : 1,
                sortable : false,
                dataIndex: 'firstname'
            },{
                text     : 'Lastname',
                flex     : 1,
                sortable : false,
                dataIndex: 'lastname'
            },{
                text     : 'Email',
                flex     : 1,
                sortable : false,
                dataIndex: 'email'
            }
        ],
        viewConfig: {
            stripeRows : true//,markDirty  : false
        },
        dockedItems: 
    	[{
            xtype: 'toolbar',
    	    items:
    	    [{
                text	: 'Add',
    	        iconCls	: 'icon-add',
    	        handler	: function()
    	        {
                    showAddNewUserForm(null, userStore);
                }
            }
    	    ,'-',
    	    {
                text	: 'Update',
    	        iconCls	: 'icon-update',
    	        handler	: function()
    	        {
                    var selection = userGrid.getView().getSelectionModel().getSelection()[0];
      	            if (selection) {
                        showAddNewUserForm(selection, userStore);
      	            }
      	            else 
      	            {
                        Ext.MessageBox.show
        	        ({    	            		
                            title   : 'Warning',
        	            msg	    : 'You must select et least one row to update!',
        		    icon    : Ext.MessageBox.WARNING,
        		    buttons : Ext.Msg.OK
        	        });
      	            }
    	        }
    	     }
             ,'-',
    	     {
                //itemId	: 'delete',
    	        text	: 'Delete',
    	        iconCls	: 'icon-delete',
    	        disabled: false,
    	        handler	: function()
    	        {
                    var selection = userGrid.getView().getSelectionModel().getSelection()[0];
    	            if (selection) {
                        userStore.remove(selection);
    	                //userStore.commitChanges();
    	                //userStore.commit();
    	                //userStore.reload();
    	                userStore.sync();
    	            }
    	            else 
    	            {
                        Ext.MessageBox.show
        	        ({    	            		
                            title   : 'Warning',
        	            msg	    : 'You must select et least one row to delete!',
        		    icon    : Ext.MessageBox.WARNING,
        		    buttons : Ext.Msg.OK
        	        });
    	            }
    	        }
    	     }
    	     ,'-',
    	     {
                text	: 'Details',
    	        iconCls	: 'icon-read',
    	        handler	: function()
    	        {
                    var selection = userGrid.getView().getSelectionModel().getSelection()[0];
      	            if (selection) 
      	            {
                        /*
                        var newUserTab = mainCenterTabs.add
      	                ({
                            closable	: true,
                            iconCls	    : 'tabs',
                            title		: 'Osobno - ' + selection.get('username') //selection.username
      	                }).show();

      	                mainCenterTabs.setActiveTab(newUserTab);*/

      	                mainMenuUserDetailsClicked(mainCenterTabs, 'DoDo1122');
      	            }
      	            else 
      	            {
                        Ext.MessageBox.show
        	        ({    	            		
                            title   : 'Warning',
        	            msg	    : 'You must select et least one row to open user details tab!',
        		    icon    : Ext.MessageBox.WARNING,
        		    buttons : Ext.Msg.OK
        	        });
      	            }
    	         }
    	      }]
    	    }],
    	    // paging bar on the bottom
            bbar: Ext.create('Ext.PagingToolbar', 
            {
                store       : userStore,
                displayInfo : true,
                displayMsg  : 'Displaying topics {0} - {1} of {2}',
                emptyMsg    : "No topics to display"
            })
        });

    // ADD MAIN USER TAB
    var newMainTab = mainCenterTabs.add
    ({
        id       : 'tab-users',
        plain    : true,
        closable : true,
        iconCls  : 'icon-group',
        title    : 'Korisnici',
        layout   : 'border',
        region   : 'center',
        items:
        [
            // LEFT
            {
                //region  : 'west',
                region    : 'center',
                layout    : 'fit',
                //plain   : true,
                margins   : '5 5 5 5',                        
                //width	  :  300,
                bodyStyle : 'background:#f1f1f1',
                items 	  : userGrid
            },
            // RIGHT
            {
                title       : 'Filter options',
                region      : 'east',
                margins     : '5 5 5 0',
                //bodyStyle   : 'background:#f1f1f1',
                width       : 250,
                minWidth    : 250,
                maxWidth    : 250,
                collapsible : true,
                collapsed   : true,
                items		: Ext.create('Ext.form.Panel', 
                {
                    region     : 'center',
                    title      : 'Generic Form Panel',
                    bodyStyle  : 'padding: 10px; background-color: #DFE8F6',
                    labelWidth : 100,
                    margins    : '0 0 0 3',
                    items      : 
                    [
                        Ext.create('Ext.form.field.Text', 
                        {
                            fieldLabel : 'Username',
                            name       : 'name1'
                        }),
                        Ext.create('Ext.form.field.Text', 
                        {
                            fieldLabel : 'Firstname / Lastname',
                            name       : 'name2'
                        })
                    ]
                }),
                dockedItems: 
                [{
                    xtype: 'toolbar',
                    items:
                    [
                        '->',
                        {
                            text	: 'Search',
                            iconCls	: 'icon-find',
                            handler	: function()
                            {

                            }
                        }
                    ]
                 }]
              }
                //,
                //CENTER
                //userTabs
         ]
    }).show();

    // set tab active
    mainCenterTabs.setActiveTab(newMainTab);
}


function showAddNewUserForm(selection, userStore) 
{
    var newUserform = Ext.widget
    ({
        //collapsible: true,
        //frame: true,
	//title: 'Simple Form',
        bodyPadding: '5 5 0',
        xtype  : 'form',
	layout : 'form',
	width  : 350,
	fieldDefaults: {
            msgTarget : 'side',
	    labelWidth: 80
	},
	defaultType: 'textfield',
	items: 
	[
            {
                fieldLabel: 'Username',
		afterLabelTextTpl: requiredStyle,
		name: 'username',
		allowBlank:false
            },{
                fieldLabel: 'Password',
		afterLabelTextTpl: requiredStyle,
		name: 'password',
		allowBlank:false
            },{
                fieldLabel: 'First Name',
	        afterLabelTextTpl: requiredStyle,
	        name: 'firstname',
	        allowBlank:false
	    },{
                fieldLabel: 'Last Name',
	        afterLabelTextTpl: requiredStyle,
	        name: 'lastname'
	    },{
                fieldLabel: 'Email',
	        //afterLabelTextTpl: requiredStyle,
	        name: 'email',
	        vtype:'email'
	    }],
             buttons: 
	     [{
                text: 'Save',
	        handler: function() 
	        {
                    // populate new user object
	            var addForm = newUserform.getForm();

	            // CREATE NEW
	            if(selection == null) 
	            {
                        var user = new User
	            	({
                            id	       : (selection != null) ? selection.get('id') : null,
                            username   : addForm.findField('username').getValue(), 
                            password   : addForm.findField('password').getValue(),
                            firstname  : addForm.findField('firstname').getValue(),
                            lastname   : addForm.findField('lastname').getValue(),
                            email      : addForm.findField('email').getValue()
	            	});
	            		
		        // store to DB
		        userStore.add(user);
		        //userStore.commitChanges();
		        //userStore.reload(); // reload new data
                    }
	            // UPDATE OLD
	            else 
	            {
                        selection.set('username', addForm.findField('username').getValue());
	            	selection.set('password', addForm.findField('password').getValue());
	            	selection.set('firstname', addForm.findField('firstname').getValue());
	            	selection.set('lastname',  addForm.findField('lastname').getValue());
	            	selection.set('email', addForm.findField('email').getValue());
	        	//selection.commit();
	            	//selection.commitChanges();
                        //userStore.reload(); // reload new data
                    }

	            // sync all data
	            //userStore.sync();
	            //userStore.reload(); // reload data ...
	            	
	            userStore.sync
	            ({
                        success : function(){},
			failure : function(response, options)
                        {
                            console.log(response.responseText); //it does not work, because there is responseText attr in response
			}  
                    });

	            // reset & close
                    this.up('form').getForm().reset();
	            this.up('window').hide();
                }
	      },
	      {
                    text    : 'Cancel',
                    handler : function() 
                    {
                        this.up('form').getForm().reset();
                        this.up('window').hide();
                    }
              }]
      });

    // if update mode, populate textboxes
    if(selection != null)
    {
        var addForm = newUserform.getForm();
	addForm.findField('username').setValue(selection.get('username'));
	addForm.findField('password').setValue(selection.get('password'));
	addForm.findField('firstname').setValue(selection.get('firstname'));
	addForm.findField('lastname').setValue(selection.get('lastname'));
	addForm.findField('email').setValue(selection.get('email'));	    	
    }

    var window = Ext.widget('window', 
    {
        title	: 'Add new user',
        width	: 400,
        height	: 400,
        layout	: 'fit',                
        modal	: true,
        items	: newUserform,
        resizable: true
    });

    window.show();
};




