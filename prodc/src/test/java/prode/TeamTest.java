package prode;

import prode.User;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TeamTest {
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
    System.out.println("TeamTest setup");
    Base.openTransaction();
  }2

  @After
  public void after(){
      System.out.println("TeamTest tearDown");
      Base.rollbackTransaction();
      Base.close();
  }

  @Test
  public void validatePrecenseOfTestnames(){
      Team team = new Team();
      team.set("teamname", "");

      assertEquals(team.isValid(), false);
  }
}