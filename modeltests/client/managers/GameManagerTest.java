/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.managers;

import client.data.*;

import shared.locations.*;
import shared.definitions.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author ddennis
 */

public class GameManagerTest {
	private String jsonDataIn = "";

	public GameManagerTest() {
		jsonDataIn = "{\"deck\":\n" + "{\"yearOfPlenty\":\n"
				+ "2,\"monopoly\":\n" + "2,\"soldier\":\n"
				+ "10,\"roadBuilding\":\n" + "1,\"monument\":\n"
				+ "4},\"map\":\n" + "{\"hexGrid\":\n" + "{\"hexes\":\n"
				+ "[[{\"isLand\":\n" + "false,\"location\":\n" + "{\"x\":\n"
				+ "0,\"y\":\n" + "-3},\"vertexes\":\n" + "[{\"value\":\n"
				+ "{\"worth\":\n" + "0,\"ownerID\":\n" + "-1}},{\"value\":\n"
				+ "{\"worth\":\n" + "0,\"ownerID\":\n" + "-1}},{\"value\":\n"
				+ "{\"worth\":\n" + "0,\"ownerID\":\n" + "-1}},{\"value\":\n"
				+ "{\"worth\":\n" + "0,\"ownerID\":\n" + "-1}},{\"value\":\n"
				+ "{\"worth\":\n" + "0,\"ownerID\":\n" + "-1}},{\"value\":\n"
				+ "{\"worth\":\n" + "0,\"ownerID\":\n" + "-1}}],\"edges\":\n"
				+ "[{\"value\":\n" + "{\"ownerID\":\n" + "-1}},{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "1,\"y\":\n" + "-3},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "2,\"y\":\n" + "-3},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "3,\"y\":\n" + "-3},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]}],[{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "-1,\"y\":\n" + "-2},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "true,\"location\":\n"
				+ "{\"x\":\n" + "0,\"y\":\n" + "-2},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Brick\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "1,\"y\":\n"
				+ "-2},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "3}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Wood\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "2,\"y\":\n"
				+ "-2},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "3}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "3}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "3,\"y\":\n" + "-2},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "2,\"ownerID\":\n"
				+ "0}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}}]}],[{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "-2,\"y\":\n" + "-1},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Brick\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "-1,\"y\":\n"
				+ "-1},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Wood\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "0,\"y\":\n"
				+ "-1},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Ore\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "1,\"y\":\n"
				+ "-1},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "3}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "2}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "3}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "2}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Sheep\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "2,\"y\":\n"
				+ "-1},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "2,\"ownerID\":\n" + "0}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"ownerID\":\n" + "0}},{\"value\":\n"
				+ "{\"ownerID\":\n" + "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "3,\"y\":\n" + "-1},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "2,\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]}],[{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "-3,\"y\":\n" + "0},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Ore\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "-2,\"y\":\n"
				+ "0},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Sheep\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "-1,\"y\":\n"
				+ "0},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Wheat\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "0,\"y\":\n"
				+ "0},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "2}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "2}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "2}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Brick\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "1,\"y\":\n"
				+ "0},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "2}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "2}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Wheat\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "2,\"y\":\n"
				+ "0},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "2,\"ownerID\":\n" + "0}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "3,\"y\":\n" + "0},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]}],[{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "-3,\"y\":\n" + "1},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Wheat\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "-2,\"y\":\n"
				+ "1},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "1}}]},{\"landtype\":\n" + "\"Sheep\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "-1,\"y\":\n"
				+ "1},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "2}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "3}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "3}}]},{\"landtype\":\n" + "\"Wood\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "0,\"y\":\n"
				+ "1},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "2}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "0}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "2}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Sheep\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "1,\"y\":\n"
				+ "1},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "0}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "2,\"ownerID\":\n" + "0}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "2,\"y\":\n" + "1},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "2,\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]}],[{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "-3,\"y\":\n" + "2},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "1,\"ownerID\":\n"
				+ "1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Wood\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "-2,\"y\":\n"
				+ "2},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "3}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "3}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Ore\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "-1,\"y\":\n"
				+ "2},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "3}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"landtype\":\n" + "\"Wheat\",\"isLand\":\n"
				+ "true,\"location\":\n" + "{\"x\":\n" + "0,\"y\":\n"
				+ "2},\"vertexes\":\n" + "[{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "1,\"ownerID\":\n" + "0}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"worth\":\n"
				+ "0,\"ownerID\":\n" + "-1}}],\"edges\":\n" + "[{\"value\":\n"
				+ "{\"ownerID\":\n" + "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "0}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "1,\"y\":\n" + "2},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]}],[{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "-3,\"y\":\n" + "3},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "-2,\"y\":\n" + "3},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "-1,\"y\":\n" + "3},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]},{\"isLand\":\n" + "false,\"location\":\n"
				+ "{\"x\":\n" + "0,\"y\":\n" + "3},\"vertexes\":\n"
				+ "[{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"worth\":\n" + "0,\"ownerID\":\n"
				+ "-1}}],\"edges\":\n" + "[{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}},{\"value\":\n" + "{\"ownerID\":\n"
				+ "-1}}]}]],\"offsets\":\n" + "[3,2,1,0,0,0,0],\"radius\":\n"
				+ "4,\"x0\":\n" + "3,\"y0\":\n" + "3},\"radius\":\n"
				+ "4,\"numbers\":\n" + "{\"2\":\n" + "[{\"x\":\n"
				+ "-2,\"y\":\n" + "1}],\"3\":\n" + "[{\"x\":\n" + "-1,\"y\":\n"
				+ "2},{\"x\":\n" + "0,\"y\":\n" + "-1}],\"4\":\n"
				+ "[{\"x\":\n" + "1,\"y\":\n" + "-2},{\"x\":\n" + "0,\"y\":\n"
				+ "1}],\"5\":\n" + "[{\"x\":\n" + "1,\"y\":\n" + "0},{\"x\":\n"
				+ "-2,\"y\":\n" + "0}],\"6\":\n" + "[{\"x\":\n" + "2,\"y\":\n"
				+ "0},{\"x\":\n" + "-2,\"y\":\n" + "2}],\"8\":\n"
				+ "[{\"x\":\n" + "0,\"y\":\n" + "2},{\"x\":\n" + "-1,\"y\":\n"
				+ "-1}],\"9\":\n" + "[{\"x\":\n" + "1,\"y\":\n"
				+ "-1},{\"x\":\n" + "-1,\"y\":\n" + "1}],\"10\":\n"
				+ "[{\"x\":\n" + "1,\"y\":\n" + "1},{\"x\":\n" + "-1,\"y\":\n"
				+ "0}],\"11\":\n" + "[{\"x\":\n" + "2,\"y\":\n"
				+ "-2},{\"x\":\n" + "0,\"y\":\n" + "0}],\"12\":\n"
				+ "[{\"x\":\n" + "2,\"y\":\n" + "-1}]},\"ports\":\n"
				+ "[{\"ratio\":\n" + "3,\"validVertex1\":\n"
				+ "{\"direction\":\n" + "\"SW\",\"x\":\n" + "3,\"y\":\n"
				+ "-3},\"validVertex2\":\n" + "{\"direction\":\n"
				+ "\"W\",\"x\":\n" + "3,\"y\":\n" + "-3},\"orientation\":\n"
				+ "\"SW\",\"location\":\n" + "{\"x\":\n" + "3,\"y\":\n"
				+ "-3}},{\"ratio\":\n" + "2,\"inputResource\":\n"
				+ "\"Wheat\",\"validVertex1\":\n" + "{\"direction\":\n"
				+ "\"SE\",\"x\":\n" + "-1,\"y\":\n" + "-2},\"validVertex2\":\n"
				+ "{\"direction\":\n" + "\"SW\",\"x\":\n" + "-1,\"y\":\n"
				+ "-2},\"orientation\":\n" + "\"S\",\"location\":\n"
				+ "{\"x\":\n" + "-1,\"y\":\n" + "-2}},{\"ratio\":\n"
				+ "2,\"inputResource\":\n" + "\"Wood\",\"validVertex1\":\n"
				+ "{\"direction\":\n" + "\"NE\",\"x\":\n" + "-3,\"y\":\n"
				+ "2},\"validVertex2\":\n" + "{\"direction\":\n"
				+ "\"E\",\"x\":\n" + "-3,\"y\":\n" + "2},\"orientation\":\n"
				+ "\"NE\",\"location\":\n" + "{\"x\":\n" + "-3,\"y\":\n"
				+ "2}},{\"ratio\":\n" + "3,\"validVertex1\":\n"
				+ "{\"direction\":\n" + "\"NW\",\"x\":\n" + "0,\"y\":\n"
				+ "3},\"validVertex2\":\n" + "{\"direction\":\n"
				+ "\"NE\",\"x\":\n" + "0,\"y\":\n" + "3},\"orientation\":\n"
				+ "\"N\",\"location\":\n" + "{\"x\":\n" + "0,\"y\":\n"
				+ "3}},{\"ratio\":\n" + "2,\"inputResource\":\n"
				+ "\"Brick\",\"validVertex1\":\n" + "{\"direction\":\n"
				+ "\"NE\",\"x\":\n" + "-2,\"y\":\n" + "3},\"validVertex2\":\n"
				+ "{\"direction\":\n" + "\"E\",\"x\":\n" + "-2,\"y\":\n"
				+ "3},\"orientation\":\n" + "\"NE\",\"location\":\n"
				+ "{\"x\":\n" + "-2,\"y\":\n" + "3}},{\"ratio\":\n"
				+ "3,\"validVertex1\":\n" + "{\"direction\":\n"
				+ "\"E\",\"x\":\n" + "-3,\"y\":\n" + "0},\"validVertex2\":\n"
				+ "{\"direction\":\n" + "\"SE\",\"x\":\n" + "-3,\"y\":\n"
				+ "0},\"orientation\":\n" + "\"SE\",\"location\":\n"
				+ "{\"x\":\n" + "-3,\"y\":\n" + "0}},{\"ratio\":\n"
				+ "2,\"inputResource\":\n" + "\"Ore\",\"validVertex1\":\n"
				+ "{\"direction\":\n" + "\"SE\",\"x\":\n" + "1,\"y\":\n"
				+ "-3},\"validVertex2\":\n" + "{\"direction\":\n"
				+ "\"SW\",\"x\":\n" + "1,\"y\":\n" + "-3},\"orientation\":\n"
				+ "\"S\",\"location\":\n" + "{\"x\":\n" + "1,\"y\":\n"
				+ "-3}},{\"ratio\":\n" + "2,\"inputResource\":\n"
				+ "\"Sheep\",\"validVertex1\":\n" + "{\"direction\":\n"
				+ "\"W\",\"x\":\n" + "3,\"y\":\n" + "-1},\"validVertex2\":\n"
				+ "{\"direction\":\n" + "\"NW\",\"x\":\n" + "3,\"y\":\n"
				+ "-1},\"orientation\":\n" + "\"NW\",\"location\":\n"
				+ "{\"x\":\n" + "3,\"y\":\n" + "-1}},{\"ratio\":\n"
				+ "3,\"validVertex1\":\n" + "{\"direction\":\n"
				+ "\"W\",\"x\":\n" + "2,\"y\":\n" + "1},\"validVertex2\":\n"
				+ "{\"direction\":\n" + "\"NW\",\"x\":\n" + "2,\"y\":\n"
				+ "1},\"orientation\":\n" + "\"NW\",\"location\":\n"
				+ "{\"x\":\n" + "2,\"y\":\n" + "1}}],\"robber\":\n"
				+ "{\"x\":\n" + "1,\"y\":\n" + "-1}},\"players\":\n"
				+ "[{\"MAX_GAME_POINTS\":\n" + "10,\"resources\":\n"
				+ "{\"brick\":\n" + "14,\"wood\":\n" + "13,\"sheep\":\n"
				+ "15,\"wheat\":\n" + "10,\"ore\":\n" + "8},\"oldDevCards\":\n"
				+ "{\"yearOfPlenty\":\n" + "0,\"monopoly\":\n"
				+ "0,\"soldier\":\n" + "2,\"roadBuilding\":\n"
				+ "0,\"monument\":\n" + "1},\"newDevCards\":\n"
				+ "{\"yearOfPlenty\":\n" + "0,\"monopoly\":\n"
				+ "0,\"soldier\":\n" + "1,\"roadBuilding\":\n"
				+ "1,\"monument\":\n" + "0},\"roads\":\n" + "8,\"cities\":\n"
				+ "2,\"settlements\":\n" + "4,\"soldiers\":\n"
				+ "1,\"victoryPoints\":\n" + "7,\"monuments\":\n"
				+ "0,\"longestRoad\":\n" + "true,\"largestArmy\":\n"
				+ "false,\"playedDevCard\":\n" + "true,\"discarded\":\n"
				+ "false,\"playerID\":\n" + "0,\"orderNumber\":\n"
				+ "0,\"name\":\n" + "\"Sam\",\"color\":\n"
				+ "\"orange\"},{\"MAX_GAME_POINTS\":\n" + "10,\"resources\":\n"
				+ "{\"brick\":\n" + "1,\"wood\":\n" + "0,\"sheep\":\n"
				+ "1,\"wheat\":\n" + "0,\"ore\":\n" + "6},\"oldDevCards\":\n"
				+ "{\"yearOfPlenty\":\n" + "0,\"monopoly\":\n"
				+ "0,\"soldier\":\n" + "0,\"roadBuilding\":\n"
				+ "0,\"monument\":\n" + "0},\"newDevCards\":\n"
				+ "{\"yearOfPlenty\":\n" + "0,\"monopoly\":\n"
				+ "0,\"soldier\":\n" + "0,\"roadBuilding\":\n"
				+ "0,\"monument\":\n" + "0},\"roads\":\n" + "13,\"cities\":\n"
				+ "4,\"settlements\":\n" + "3,\"soldiers\":\n"
				+ "0,\"victoryPoints\":\n" + "2,\"monuments\":\n"
				+ "0,\"longestRoad\":\n" + "false,\"largestArmy\":\n"
				+ "false,\"playedDevCard\":\n" + "false,\"discarded\":\n"
				+ "false,\"playerID\":\n" + "1,\"orderNumber\":\n"
				+ "1,\"name\":\n" + "\"Brooke\",\"color\":\n"
				+ "\"blue\"},{\"MAX_GAME_POINTS\":\n" + "10,\"resources\":\n"
				+ "{\"brick\":\n" + "5,\"wood\":\n" + "1,\"sheep\":\n"
				+ "0,\"wheat\":\n" + "1,\"ore\":\n" + "0},\"oldDevCards\":\n"
				+ "{\"yearOfPlenty\":\n" + "0,\"monopoly\":\n"
				+ "0,\"soldier\":\n" + "0,\"roadBuilding\":\n"
				+ "0,\"monument\":\n" + "0},\"newDevCards\":\n"
				+ "{\"yearOfPlenty\":\n" + "0,\"monopoly\":\n"
				+ "0,\"soldier\":\n" + "0,\"roadBuilding\":\n"
				+ "0,\"monument\":\n" + "0},\"roads\":\n" + "13,\"cities\":\n"
				+ "4,\"settlements\":\n" + "3,\"soldiers\":\n"
				+ "0,\"victoryPoints\":\n" + "2,\"monuments\":\n"
				+ "0,\"longestRoad\":\n" + "false,\"largestArmy\":\n"
				+ "false,\"playedDevCard\":\n" + "false,\"discarded\":\n"
				+ "false,\"playerID\":\n" + "10,\"orderNumber\":\n"
				+ "2,\"name\":\n" + "\"Pete\",\"color\":\n"
				+ "\"red\"},{\"MAX_GAME_POINTS\":\n" + "10,\"resources\":\n"
				+ "{\"brick\":\n" + "0,\"wood\":\n" + "1,\"sheep\":\n"
				+ "1,\"wheat\":\n" + "0,\"ore\":\n" + "2},\"oldDevCards\":\n"
				+ "{\"yearOfPlenty\":\n" + "0,\"monopoly\":\n"
				+ "0,\"soldier\":\n" + "0,\"roadBuilding\":\n"
				+ "0,\"monument\":\n" + "0},\"newDevCards\":\n"
				+ "{\"yearOfPlenty\":\n" + "0,\"monopoly\":\n"
				+ "0,\"soldier\":\n" + "0,\"roadBuilding\":\n"
				+ "0,\"monument\":\n" + "0},\"roads\":\n" + "13,\"cities\":\n"
				+ "4,\"settlements\":\n" + "3,\"soldiers\":\n"
				+ "0,\"victoryPoints\":\n" + "2,\"monuments\":\n"
				+ "0,\"longestRoad\":\n" + "false,\"largestArmy\":\n"
				+ "false,\"playedDevCard\":\n" + "false,\"discarded\":\n"
				+ "false,\"playerID\":\n" + "11,\"orderNumber\":\n"
				+ "3,\"name\":\n" + "\"Mark\",\"color\":\n"
				+ "\"green\"}],\"log\":\n" + "{\"lines\":\n"
				+ "[{\"source\":\n" + "\"Sam\",\"message\":\n"
				+ "\"Sam built a road\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam built a settlement\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Brooke\",\"message\":\n"
				+ "\"Brooke built a road\"},{\"source\":\n"
				+ "\"Brooke\",\"message\":\n"
				+ "\"Brooke built a settlement\"},{\"source\":\n"
				+ "\"Brooke\",\"message\":\n"
				+ "\"Brooke\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Pete\",\"message\":\n"
				+ "\"Pete built a road\"},{\"source\":\n"
				+ "\"Pete\",\"message\":\n"
				+ "\"Pete built a settlement\"},{\"source\":\n"
				+ "\"Pete\",\"message\":\n"
				+ "\"Pete\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark built a road\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark built a settlement\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark built a road\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark built a settlement\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Pete\",\"message\":\n"
				+ "\"Pete built a road\"},{\"source\":\n"
				+ "\"Pete\",\"message\":\n"
				+ "\"Pete built a settlement\"},{\"source\":\n"
				+ "\"Pete\",\"message\":\n"
				+ "\"Pete\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Brooke\",\"message\":\n"
				+ "\"Brooke built a road\"},{\"source\":\n"
				+ "\"Brooke\",\"message\":\n"
				+ "\"Brooke built a settlement\"},{\"source\":\n"
				+ "\"Brooke\",\"message\":\n"
				+ "\"Brooke\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam built a road\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam built a settlement\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam rolled a 3.\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam built a road\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam built a road\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam built a road\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam built a road\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam built a road\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam upgraded to a city\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam built a settlement\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam upgraded to a city\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam bought a Development Card.\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam bought a Development Card.\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam bought a Development Card.\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam bought a Development Card.\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Brooke\",\"message\":\n"
				+ "\"Brooke rolled a 5.\"},{\"source\":\n"
				+ "\"Brooke\",\"message\":\n"
				+ "\"Brooke\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Pete\",\"message\":\n"
				+ "\"Pete rolled a 5.\"},{\"source\":\n"
				+ "\"Pete\",\"message\":\n"
				+ "\"Pete\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark rolled a 5.\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark rolled a 5.\"},{\"source\":\n"
				+ "\"Mark\",\"message\":\n"
				+ "\"Mark\\u0027s turn just ended\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam rolled a 5.\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam bought a Development Card.\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam bought a Development Card.\"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam used a soldier \"},{\"source\":\n"
				+ "\"Sam\",\"message\":\n"
				+ "\"Sam moved the robber and robbed Pete.\"}]},\"chat\":\n"
				+ "{\"lines\":\n" + "[]},\"bank\":\n" + "{\"brick\":\n"
				+ "4,\"wood\":\n" + "9,\"sheep\":\n" + "1,\"wheat\":\n"
				+ "7,\"ore\":\n" + "2},\"turnTracker\":\n" + "{\"status\":\n"
				+ "\"Playing\",\"currentTurn\":\n" + "0},\"largestArmy\":\n"
				+ "2,\"longestRoad\":\n" + "0,\"winner\":\n" + "-1}";
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}
        
	@Test
	public void testCanBuyDevCard() {
		GameManager instance = new GameManager();
		instance.initializeGame(jsonDataIn);

		Game game = instance.getGame();

		// CurrentPlayer is Sam. Sam has enough resources for a DevCard
		game.getTurnTracker().setCurrentTurn(0);

		DevCardList prevDevCards = instance.getResourceManager().getGameBanks()
				.get(0).getUnusableDevCards();
		assert (prevDevCards.totalCardsRemaining() == 0);

		ResourceList prevResources = instance.getResourceManager()
				.getGameBanks().get(0).getResourcesCards();

		String previousResourceString = (prevResources.toString());

		// Verified devCardList was empty, now if we buy a card it should have
		// one.
		instance.buyDevCard();
		DevCardList newDevCards = instance.getResourceManager().getGameBanks()
				.get(0).getUnusableDevCards();
		ResourceList newResources = instance.getResourceManager()
				.getGameBanks().get(0).getResourcesCards();
		;
		assert (newDevCards.totalCardsRemaining() == 1);

		// If we add the resources back and the result is equal to the previous
		// quantities, then they had been removed properly
		newResources.addOre(1);
		newResources.addWheat(1);
		newResources.addSheep(1);
		assert (newResources.toString().equals(previousResourceString));

	}

	@Test
	public void testPlayMonument() {
		GameManager target = new GameManager();
		target.initializeGame(jsonDataIn);
		DevCardList cards = new DevCardList(1, 1, 1, 1, 1);
		List<Bank> banks = target.getResourceManager().getGameBanks();
		banks.get(0).setDevelopmentCards(cards);
		target.getResourceManager().setGameBanks(banks);

		int playedBefore = target.getResourceManager().getGameBanks().get(0)
				.getMonuments();
		int heldBefore = target.getResourceManager().getGameBanks().get(0)
				.getDevelopmentCards().getMonument();
		target.useMonument();
		int playedAfter = target.getResourceManager().getGameBanks().get(0)
				.getMonuments();
		int heldAfter = target.getResourceManager().getGameBanks().get(0)
				.getDevelopmentCards().getMonument();
		assertTrue(playedBefore + 1 == playedAfter);
		assertTrue(heldAfter + 1 == heldBefore);
	}

	@Test
	public void testPlaySoldier() {
		GameManager target = new GameManager();
		target.initializeGame(jsonDataIn);
		DevCardList cards = new DevCardList(1, 1, 1, 1, 1);
		List<Bank> banks = target.getResourceManager().getGameBanks();
		banks.get(0).setDevelopmentCards(cards);
		target.getResourceManager().setGameBanks(banks);

		int playedBefore = target.getResourceManager().getGameBanks().get(0)
				.getSoldiers();
		int heldBefore = target.getResourceManager().getGameBanks().get(0)
				.getDevelopmentCards().getSoldier();
		target.useSoldier(new HexLocation(0, 0));
		int playedAfter = target.getResourceManager().getGameBanks().get(0)
				.getSoldiers();
		int heldAfter = target.getResourceManager().getGameBanks().get(0)
				.getDevelopmentCards().getSoldier();
		assertTrue(playedBefore + 1 == playedAfter);
		assertTrue(heldAfter + 1 == heldBefore);
	}

	@Test
	public void testPlayRoadBuilding() {
		GameManager target = new GameManager();
		target.initializeGame(jsonDataIn);
		DevCardList cards = new DevCardList(1, 1, 1, 1, 1);
		List<Bank> banks = target.getResourceManager().getGameBanks();
		banks.get(0).setDevelopmentCards(cards);
		target.getResourceManager().setGameBanks(banks);

		int brickBefore = target.getResourceManager().getGameBanks().get(0)
				.getResourcesCards().getBrick();
		int heldBefore = target.getResourceManager().getGameBanks().get(0)
				.getDevelopmentCards().getRoadBuilding();

		target.useRoadBuilding(new EdgeLocation(new HexLocation(0, 0),
				EdgeDirection.NE), new EdgeLocation(new HexLocation(0, 0),
				EdgeDirection.NE));
		int brickAfter = target.getResourceManager().getGameBanks().get(0)
				.getResourcesCards().getBrick();
		int heldAfter = target.getResourceManager().getGameBanks().get(0)
				.getDevelopmentCards().getRoadBuilding();

		//if there is exactly one less roadbuilding card now than before, it worked.
		assert(heldAfter + 1 == heldBefore);
		//if no bricks were removed from the resources, then it called the right method to play roads for free.
		assert(brickAfter == brickBefore);
	}

	@Test
	public void testPlayYearOfPlenty() {
		GameManager target = new GameManager();
		target.initializeGame(jsonDataIn);
		DevCardList cards = new DevCardList(1, 1, 1, 1, 1);
		List<Bank> banks = target.getResourceManager().getGameBanks();
		banks.get(0).setDevelopmentCards(cards);
		target.getResourceManager().setGameBanks(banks);

		int brickBefore = target.getResourceManager().getGameBanks().get(0)
				.getResourcesCards().getBrick();
		int heldBefore = target.getResourceManager().getGameBanks().get(0)
				.getDevelopmentCards().getYearOfPlenty();

		target.useYearOfPlenty(ResourceType.brick, ResourceType.brick);
		int brickAfter = target.getResourceManager().getGameBanks().get(0)
				.getResourcesCards().getBrick();
		int heldAfter = target.getResourceManager().getGameBanks().get(0)
				.getDevelopmentCards().getYearOfPlenty();

		//if there is exactly one less roadbuilding card now than before, it worked.
		assert(heldAfter + 1 == heldBefore);
		assert(brickAfter - 2 == brickBefore);
	}

	@Test
	public void testPlayMonopoly() {
		GameManager target = new GameManager();
		target.initializeGame(jsonDataIn);
		DevCardList cards = new DevCardList(1, 1, 1, 1, 1);
		List<Bank> banks = target.getResourceManager().getGameBanks();
		banks.get(0).setDevelopmentCards(cards);
		target.getResourceManager().setGameBanks(banks);

		int userBrickBefore = target.getResourceManager().getGameBanks().get(0)
				.getResourcesCards().getBrick();

		target.useMonopoly(ResourceType.brick);
		int userBrickAfter = target.getResourceManager().getGameBanks().get(0)
				.getResourcesCards().getBrick();

		for(int i = 1; i < 4; i++) {
			ResourceList r = target.getResourceManager().getGameBanks().get(i).getResourcesCards();
			assert(r.getBrick() == 0);
		}
		//if there is exactly one less roadbuilding card now than before, it worked.
		assert(userBrickAfter > userBrickBefore);
	}

	@Test
	public void testCreateGame() {
		GameManager game = new GameManager();
		game.createGame(false, false, false, "Muahahahahhahahhahha");
		assertTrue(game.getGame().getTitle().equals("Muahahahahhahahhahha"));
		assertFalse(game.getGame().getTitle().equals("Milk"));
		List<Hex> hexesBasic = game.getMapManager().getHexList();
		List<Location> locationsBasic = game.getLocationManager()
				.getUnsettledLocations();
		List<Edge> edgesBasic = game.getLocationManager().getUnsettledEdges();
		List<Port> portsBasic = game.getLocationManager().getPorts();
		game = new GameManager();
		game.createGame(false, false, false, "Muahahahahhahahhahha");
		for (int i = 0; i < game.getMapManager().getHexList().size(); i++) {
			assertTrue(hexesBasic
					.get(i)
					.getLocation()
					.equals(game.getMapManager().getHexList().get(i)
							.getLocation()));
			assertTrue(hexesBasic.get(i).getNumber() == game.getMapManager()
					.getHexList().get(i).getNumber());
		}
		for (int i = 0; i < game.getLocationManager().getPorts().size(); i++) {
			assertTrue(portsBasic
					.get(i)
					.getLocation()
					.equals(game.getLocationManager().getPorts().get(i)
							.getLocation()));
		}
		for (int i = 0; i < game.getLocationManager().getUnsettledLocations()
				.size(); i++) {
			assertTrue(locationsBasic
					.get(i)
					.getNormalizedLocation()
					.equals(game.getLocationManager().getUnsettledLocations()
							.get(i).getNormalizedLocation()));
		}
		for (int i = 0; i < game.getLocationManager().getUnsettledEdges()
				.size(); i++) {
			assertTrue(edgesBasic
					.get(i)
					.getEdgeLocation()
					.equals(game.getLocationManager().getUnsettledEdges()
							.get(i).getEdgeLocation()));
		}
	}
}
