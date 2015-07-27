/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

/**
 *
 * @author ddennis
 */
public class CreateGameRequest {

    private Boolean randomTiles;
    private Boolean randomNumbers;
    private Boolean randomPorts;
    private String name;

    public CreateGameRequest(Boolean randomTiles, Boolean randomNumbers, Boolean randomPorts, String name) {
        this.randomTiles = randomTiles;
        this.randomNumbers = randomNumbers;
        this.randomPorts = randomPorts;
        this.name = name;
    }

    public Boolean isRandomTiles() {
        return randomTiles;
    }

    public void setRandomTiles(Boolean randomTiles) {
        this.randomTiles = randomTiles;
    }

    public Boolean isRandomNumbers() {
        return randomNumbers;
    }

    public void setRandomNumbers(Boolean randomNumbers) {
        this.randomNumbers = randomNumbers;
    }

    public Boolean isRandomPorts() {
        return randomPorts;
    }

    public void setRandomPorts(Boolean randomPorts) {
        this.randomPorts = randomPorts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
