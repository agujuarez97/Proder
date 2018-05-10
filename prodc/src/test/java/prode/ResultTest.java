package prode;

import prode.Result;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResultTest{
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
    System.out.println("ResultTest setup");
    Base.openTransaction();
  }

  @After
  public void after(){
      System.out.println("ResultTest tearDown");
      Base.rollbackTransaction();
      Base.close();
  }

  /*@Test*/
}