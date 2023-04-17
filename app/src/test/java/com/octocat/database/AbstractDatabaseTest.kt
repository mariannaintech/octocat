package com.octocat.database

import android.os.Build.VERSION_CODES.M
import androidx.room.Room
import com.octocat.base.BaseTest
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [M])
abstract class AbstractDatabaseTest : BaseTest() {
  lateinit var db: OctocatDatabase

  @Before
  fun initDB() {
    db = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.getApplication(), OctocatDatabase::class.java)
      .allowMainThreadQueries()
      .build()
  }

  @After
  fun closeDB() {
    db.close()
  }
}
