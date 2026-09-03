package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.CitizenReportDao
import com.example.data.local.SunuDatabase
import com.example.data.model.CitizenReportEntity
import com.example.data.model.IncidentCategory
import com.example.data.repository.TransitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ReportIncidentRoomTest {

    private lateinit var database: SunuDatabase
    private lateinit var reportDao: CitizenReportDao
    private lateinit var repository: TransitRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            SunuDatabase::class.java
        ).allowMainThreadQueries().build()

        reportDao = database.citizenReportDao()
        repository = TransitRepository(
            ticketDao = database.ticketDao(),
            reportDao = reportDao,
            passDao = database.passDao()
        )
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieveAccidentReport() = runBlocking {
        val report = CitizenReportEntity(
            category = "Accident",
            locationName = "Échangeur Malick Sy",
            description = "Collision matérielle entre deux véhicules, voie droite bloquée.",
            severity = "Critique",
            timestamp = System.currentTimeMillis(),
            confirmationsCount = 3,
            authorName = "Modou Diop"
        )

        val reportId = reportDao.insertReport(report)
        assertTrue(reportId > 0)

        val allReports = reportDao.getAllReports().first()
        assertEquals(1, allReports.size)
        val retrieved = allReports.first()
        assertEquals("Accident", retrieved.category)
        assertEquals("Échangeur Malick Sy", retrieved.locationName)
        assertEquals("Critique", retrieved.severity)
        assertEquals(3, retrieved.confirmationsCount)
    }

    @Test
    fun testQueryReportsByCategory() = runBlocking {
        reportDao.insertReport(
            CitizenReportEntity(
                category = "Accident",
                locationName = "Pont de l'Émergence",
                description = "Voie bloquée",
                severity = "Critique"
            )
        )
        reportDao.insertReport(
            CitizenReportEntity(
                category = "Trafic",
                locationName = "Rond-point Patte d'Oie",
                description = "Bouchon dense depuis 25 min",
                severity = "Important"
            )
        )
        reportDao.insertReport(
            CitizenReportEntity(
                category = "Sécurité",
                locationName = "Passerelle Baux Maraîchers",
                description = "Éclairage éteint, zone sombre",
                severity = "Important"
            )
        )

        val accidents = reportDao.getReportsByCategory("Accident").first()
        assertEquals(1, accidents.size)
        assertEquals("Pont de l'Émergence", accidents.first().locationName)

        val traffic = reportDao.getReportsByCategory("Trafic").first()
        assertEquals(1, traffic.size)
        assertEquals("Rond-point Patte d'Oie", traffic.first().locationName)

        val safety = reportDao.getReportsByCategory("Sécurité").first()
        assertEquals(1, safety.size)
        assertEquals("Passerelle Baux Maraîchers", safety.first().locationName)
    }

    @Test
    fun testConfirmReportIncrementsCount() = runBlocking {
        val id = reportDao.insertReport(
            CitizenReportEntity(
                category = "Trafic",
                locationName = "Corniche Ouest",
                description = "Ralentissement important",
                severity = "Important",
                confirmationsCount = 2
            )
        )

        reportDao.confirmReport(id.toInt())
        val updated = reportDao.getAllReports().first().first { it.id == id.toInt() }
        assertEquals(3, updated.confirmationsCount)
    }

    @Test
    fun testDeleteReport() = runBlocking {
        val id = reportDao.insertReport(
            CitizenReportEntity(
                category = "Sécurité",
                locationName = "Terminus Petersen",
                description = "Test report to delete",
                severity = "Modéré"
            )
        )

        var list = reportDao.getAllReports().first()
        assertEquals(1, list.size)

        reportDao.deleteReportById(id.toInt())
        list = reportDao.getAllReports().first()
        assertTrue(list.isEmpty())
    }

    @Test
    fun testRepositoryAddAndConfirmCitizenReport() = runBlocking {
        val created = repository.addCitizenReport(
            category = "Accident",
            locationName = "Autoroute Sortie 9",
            description = "Camion en panne sur la bande d'arrêt",
            severity = "Important",
            authorName = "Fatou Fall"
        )

        assertNotNull(created)
        assertTrue(created.id > 0)
        assertEquals("Accident", created.category)
        assertEquals("Autoroute Sortie 9", created.locationName)
        assertEquals("Fatou Fall", created.authorName)

        val list = repository.allReports.first()
        assertEquals(1, list.size)

        repository.confirmReport(created.id)
        val afterConfirm = repository.allReports.first()
        assertEquals(2, afterConfirm.first().confirmationsCount)
    }

    @Test
    fun testIncidentCategoryEnumHelpers() {
        val accident = IncidentCategory.fromCategoryName("Accident")
        assertEquals(IncidentCategory.ACCIDENT, accident)
        assertEquals("🚨", accident.emoji)

        val traffic = IncidentCategory.fromCategoryName("Trafic")
        assertEquals(IncidentCategory.TRAFFIC, traffic)
        assertEquals("🚦", traffic.emoji)

        val safety = IncidentCategory.fromCategoryName("Sécurité")
        assertEquals(IncidentCategory.SAFETY, safety)
        assertEquals("🛡️", safety.emoji)
    }
}
