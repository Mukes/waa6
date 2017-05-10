package com.lab6;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 985552 on 5/9/2017.
 */
@ApplicationPath("api")
public class RestApplication extends Application{
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add( AccountController.class );
        return h;
    }
}
