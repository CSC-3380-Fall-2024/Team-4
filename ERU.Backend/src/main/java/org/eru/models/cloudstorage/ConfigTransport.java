package org.eru.models.cloudstorage;

public class ConfigTransport {
    public String Name;
    public String Type;
    public String AppName = "Eru";
    public boolean IsEnabled;
    public boolean IsRequired;
    public boolean IsPrimary;
    public int TimeoutSeconds = 30;
    public int Priority;

    public ConfigTransport(String name, String type, boolean isEnabled, int priority) {
        Name = name;
        Type = type;
        IsEnabled = isEnabled;
        IsRequired = isEnabled;
        IsPrimary = isEnabled;
        Priority = priority;
    }
}
