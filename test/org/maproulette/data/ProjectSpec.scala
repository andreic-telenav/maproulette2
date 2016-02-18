package org.maproulette.data

import org.junit.runner.RunWith
import org.maproulette.data.dal.ProjectDAL
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.Json
import play.api.test.WithApplication

/**
  * @author cuthbertm
  */
@RunWith(classOf[JUnitRunner])
class ProjectSpec extends Specification {
  implicit var projectID:Long = -1

  sequential

  "Projects" should {
    "write project object to database" in new WithApplication {
      val newProject = Project(projectID, "NewProject_projecttest", Some("This is a newProject"))
      projectID = ProjectDAL.insert(newProject).id
      ProjectDAL.retrieveById match {
        case Some(t) =>
          t.name mustEqual newProject.name
          t.description mustEqual newProject.description
        case None =>
          // fail here automatically because we should have retrieved the tag
          1 mustEqual 2
      }
    }

    "update project object to database" in new WithApplication {
      ProjectDAL.update(Json.parse(
        """{
          "name":"UpdatedProject"
        }""".stripMargin))(projectID)
      ProjectDAL.retrieveById match {
        case Some(t) =>
          t.name mustEqual "UpdatedProject"
          t.id mustEqual projectID
        case None =>
          // fail here automatically because we should have retrieved the tag
          1 mustEqual 2
      }
    }

    "delete project object in database" in new WithApplication {
      implicit val ids = List(projectID)
      ProjectDAL.delete
      ProjectDAL.retrieveById mustEqual None
    }
  }
}
