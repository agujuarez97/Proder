package prode;

import prode.Prediction;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PredictionTest{
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
    System.out.println("PredictionTest setup");
    Base.openTransaction();
  }

  @After
  public void after(){
      System.out.println("PredictionTest tearDown");
      Base.rollbackTransaction();
      Base.close();
  }

  /*@Test*/
}