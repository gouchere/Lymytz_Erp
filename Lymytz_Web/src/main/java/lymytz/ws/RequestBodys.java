/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws;

import java.io.Serializable;

/**
 *
 * @author Lymytz Dowes
 */
public class RequestBodys implements Serializable {

    RequestBody request1 = new RequestBody();
    RequestBody request2 = new RequestBody();

    public RequestBodys() {
    }

    public class RequestBody implements Serializable {

        String hello;
        String foo;
        Integer count;

        public RequestBody() {
        }
    }
}
