Ext.define('User', 
{
    extend: 'Ext.data.Model',
    proxy: 
    {
        url  : WS_URL + 'users',
        type : 'rest',        
        startParam     : 'offset',
        limitParam     : 'limit',
        sortParam      : 'sort',
        directionParam : 'order',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        },
        writer: Ext.create('Ext.data.writer.Json',
        {
            getRecordData: function(record) 
            {
                return {
                    'idadmin'	: record.data.idadmin,
                    'username'	: record.data.username,
                    'password'	: record.data.password,
                    'firstname'	: record.data.firstname,
                    'lastname'	: record.data.lastname,
                    'idRoles'	: record.data.idRoles
                };        
            }
        }),
        listeners: 
        {
            exception: restException
        }
    },
    /*
    listeners: 
    {
        write: restWrite
    },*/
    idProperty : 'id',
    fields:
    [
        { name: 'id',        type: 'int',       useNull: true, optional: true, defaultValue: null },
        { name: 'username',  type: 'string', 	useNull: true, optional: true, defaultValue: null },
        { name: 'firstname', type: 'string', 	useNull: true, optional: true, defaultValue: null },
        { name: 'lastname',  type: 'string', 	useNull: true, optional: true, defaultValue: null },
        { name: 'password',  type: 'string', 	useNull: true, optional: true, defaultValue: null },
        { name: 'email',     type: 'string', 	useNull: true, optional: true, defaultValue: null }        
    ]
});

