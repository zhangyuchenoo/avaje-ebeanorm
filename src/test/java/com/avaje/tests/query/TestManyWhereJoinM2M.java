package com.avaje.tests.query;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.avaje.ebean.BaseTestCase;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.tests.model.basic.MRole;
import com.avaje.tests.model.basic.MUser;

import static org.assertj.core.api.Assertions.assertThat;

public class TestManyWhereJoinM2M extends BaseTestCase {

  @Test
  public void test() {

    Ebean.beginTransaction();

    MRole r1 = new MRole();
    r1.setRoleName("role1");
    Ebean.save(r1);

    MRole r2 = new MRole();
    r2.setRoleName("role2special");
    Ebean.save(r2);

    MRole r3 = new MRole();
    r3.setRoleName("role3");
    Ebean.save(r3);

    MUser u0 = new MUser();
    u0.setUserName("user0");
    u0.addRole(r1);
    u0.addRole(r2);

    Ebean.save(u0);

    MUser u1 = new MUser();
    u1.setUserName("user1");
    u1.addRole(r1);

    Ebean.save(u1);

    MUser u2 = new MUser();
    u2.setUserName("user2");
    Ebean.save(u2);

    Ebean.commitTransaction();

    Query<MUser> query = Ebean.find(MUser.class).fetch("roles")
    // the where on a 'many' (like orders) requires an
    // additional join and distinct which is independent
    // of a fetch join (if there is a fetch join)
        .where().eq("roles.roleName", "role2special").query();

    query.findList();

    String sql = query.getGeneratedSql();
    Assert.assertTrue(sql.contains("select distinct"));
    Assert.assertTrue(sql.contains("left outer join mrole "));
    Assert.assertTrue(sql.contains("join mrole "));
    Assert.assertTrue(sql.contains(".role_name = ?"));

    isEmpty();
    isNotEmpty();
  }

  private void isEmpty() {

    Query<MUser> query = Ebean.find(MUser.class)
        .where().isEmpty("roles")
        .query();

    List<MUser> usersWithNoRoles = query.findList();

    assertThat(query.getGeneratedSql()).contains("select t0.userid c0, t0.user_name c1, t0.user_type_id c2 from muser t0 where not exists (select 1 from mrole_muser where muser_userid = t0.userid)");
    assertThat(usersWithNoRoles).isNotEmpty();
  }

  private void isNotEmpty() {

    Query<MUser> query = Ebean.find(MUser.class)
        .select("userName")
        .where().isNotEmpty("roles")
        .query();

    List<MUser> usersWithRoles = query.findList();

    assertThat(query.getGeneratedSql()).contains("select t0.userid c0, t0.user_name c1 from muser t0 where exists (select 1 from mrole_muser where muser_userid = t0.userid)");
    assertThat(usersWithRoles).isNotEmpty();
  }
}
