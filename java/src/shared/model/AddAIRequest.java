/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.io.Serializable;

/**
 *
 * @author ddennis
 */
public class AddAIRequest implements Serializable {

    private String AIType;

    public AddAIRequest(String AIType) {
        this.AIType = AIType;
    }

    public String getAIType() {
        return AIType;
    }

    public void setAIType(String AIType) {
        this.AIType = AIType;
    }

}
