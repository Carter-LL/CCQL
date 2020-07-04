# CQL (Champ's Query Language) JDBC,JAVASQL
A simplier way, of Java SQL, Created using intellij.

# Initialize
```
public void Main() {
    CQL_INIT.INIT(host, port, database, username, password);
}
```

# Insert
```
public void Call() {
    CQL cql = new CQL(CQL_STATEMENT.INSERT, new Table(String), "?","?","?","?","?","?","?","?","?","default"); //Infinite
    cql.FINDABLE(1, "Hello", "World", 2, 3, 4, 5, 6, "7"); //Infinite
    cql.EXECUTE();
    cql.CLOSE_CONNECTION();
}
```

# Insert with outside Properties
```
public void Call() {
    CQL_PROPERTY CqlProperty = new CQL_PROPERTY(CQL_PROPERTIES.AUTOCLOSE, CQL_PROPERTIES.AUTOEXECUTE);
    CQL cql = new CQL(CQL_STATEMENT.INSERT, CqlProperty, new Table(String), "?","?","?","?","?","?","?","?","?","default"); //Infinite
    cql.FINDABLE(1, "Hello", "World", 2, 3, 4, 5, 6, "7"); //Infinite
}
```

# Update
```
public void Call() {
    CQL cql = new CQL(CQL_STATEMENT.UPDATE, new Table(String), "Age", "35", new Where(Name), "JohnSmith"); 
    cql.EXECUTE();
    cql.CLOSE_CONNECTION();
}
```

# Select
```
public void Call() {
    CQL cql = new CQL(CQL_STATEMENT.SELECT, new Table(String), 
            new CQL_WHERE("Number"), 
            new CQL_WHERE("1"), //where Number = 1
            "Name", "Age", "Birthdate", "BornAt", "LastName",
            "MiddleName", "Guardian", "Legal", "Gender");
            
    cql.EXECUTE();
    cql.CLOSE_CONNECTION();
    
    for(Map.Entry<Integer, Hashtable<String, String>> entry : cql.data.entrySet()){
        Hashtable<String, String> d = new Hashtable<String, String>();
        d = cql.data.get(entry.getKey());
        
        String Name = (String)d.get("Name");
        int Age = (int)d.get("Age");
        String Birthdate (String)d.get("Birthdate");
    }
    
}
```


