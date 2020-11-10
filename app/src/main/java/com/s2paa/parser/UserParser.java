package com.s2paa.parser;

import com.s2paa.Model.User;

/**
 * Created by admin on 8/8/2017.
 */

public  class UserParser {


    public class Response {
        public User me;
    }

    public Response data;

    public User getUser() {
        return this.data.me;
    }
     public boolean isValidResponse(){
         if (this.data != null && this.data.me != null)
             return true;
         return false;
     }
}
