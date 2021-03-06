from peewee import *
from passlib.hash import sha256_crypt
from datetime import timedelta
import unittest
import imp
import datetime
import sys

testDatabase = imp.load_source('testDatabase', 'Website/justHealthServer/testDatabase.py')

# Import the api so we are able to run locally
sys.path.insert(0, 'Website')
import justHealthServer
from justHealthServer import api

class testCreatePrescriptionInstances(unittest.TestCase):
    """Testing the getAppointmentsDueNow API method"""

    def setUp(self):
        """Create all the tables that are needed"""
        testDatabase.createAll()

        #create test user 1
        patientClient = testDatabase.Client.insert(
            username = "patient",
            email = "justhealth123@richlogan.co.uk",
            dob = "03/03/1993",
            verified = True,
            accountlocked = False,
            loginattempts = 0,
            accountdeactivated = False)
        patientClient.execute()

        testPatient = testDatabase.Patient.insert(
            username = "patient",
            firstname = "patient",
            surname = "patient",
            ismale = True)
        testPatient.execute()

        patientPassword = testDatabase.uq8LnAWi7D.insert(
            expirydate = '01/01/2020',
            iscurrent = True,
            password = sha256_crypt.encrypt('test'),
            username = "patient")
        patientPassword.execute()

        testMedication = testDatabase.Medication.insert(
            name = "test")
        testMedication.execute()

        testPrescription = testDatabase.Prescription.insert(
            username = "patient",  
            medication = "test",
            dosage = 1,
            dosageunit = "test",
            quantity = 1,
            startdate = datetime.datetime.now().date(),
            enddate = '01/01/2020',
            stockleft = 100,
            prerequisite = "test",
            dosageform = "test",
            frequency = 1,
            Monday = True,
            Tuesday = True,
            Wednesday = True,
            Thursday = True, 
            Friday = True, 
            Saturday = True,
            Sunday = True
        )
        testPrescription.execute()

        #create test user 2
        patientClient2 = testDatabase.Client.insert(
            username = "patient2",
            email = "justhealth123@sjtate.co.uk",
            dob = "03/03/1993",
            verified = True,
            accountlocked = False,
            loginattempts = 0,
            accountdeactivated = False)
        patientClient2.execute()

        testPatient2 = testDatabase.Patient.insert(
            username = "patient2",
            firstname = "patient",
            surname = "patient",
            ismale = True)
        testPatient2.execute()

        patientPassword2 = testDatabase.uq8LnAWi7D.insert(
            expirydate = '01/01/2020',
            iscurrent = True,
            password = sha256_crypt.encrypt('test'),
            username = "patient2")
        patientPassword2.execute()

        testPrescription2 = testDatabase.Prescription.insert(
            prescriptionid = 2,
            username = "patient2",  
            medication = "test",
            dosage = 1,
            dosageunit = "test",
            quantity = 1,
            startdate = datetime.datetime.now().date() + datetime.timedelta(days = 3),
            enddate = '01/01/2020',
            stockleft = 1,
            prerequisite = "test",
            dosageform = "test",
            frequency = 1,
            Monday = True,
            Tuesday = True,
            Wednesday = True,
            Thursday = True, 
            Friday = True, 
            Saturday = True,
            Sunday = True
            )
        testPrescription2.execute()

        #create test user 3
        patientClient3 = testDatabase.Client.insert(
            username = "patient3",
            email = "justhealth123@richlogan.co.uk",
            dob = "03/03/1993",
            verified = True,
            accountlocked = False,
            loginattempts = 0,
            accountdeactivated = False)
        patientClient3.execute()

        testPatient3 = testDatabase.Patient.insert(
            username = "patient3",
            firstname = "patient",
            surname = "patient",
            ismale = True)
        testPatient3.execute()

        patientPassword3 = testDatabase.uq8LnAWi7D.insert(
            expirydate = '01/01/2020',
            iscurrent = True,
            password = sha256_crypt.encrypt('test'),
            username = "patient3")
        patientPassword3.execute()

        testPrescription3 = testDatabase.Prescription.insert(
            prescriptionid = 3,
            username = "patient3",  
            medication = "test",
            dosage = 1,
            dosageunit = "test",
            quantity = 1,
            startdate = datetime.datetime.now().date(),
            enddate = '01/01/2020',
            stockleft = 100,
            prerequisite = "test",
            dosageform = "test",
            frequency = 1,
            Monday = True,
            Tuesday = True,
            Wednesday = True,
            Thursday = True, 
            Friday = True, 
            Saturday = True,
            Sunday = True
        )
        testPrescription3.execute()

        takePrescription3 = testDatabase.TakePrescription.insert(
            prescriptionid = 3,
            currentcount = 0,
            startingcount = 100,
            currentdate = datetime.datetime.now().date()
        )
        takePrescription3.execute()

    def testLegitimate(self):
        """Attempt to check an prescription that is due to be taken today"""
        api.createTakePrescriptionInstances('patient', datetime.datetime.now())
        self.assertEqual(testDatabase.TakePrescription.select().count(),2)

    def testNotToday(self):
        """Attempt to check a prescription that is not due to be taken today"""
        api.createTakePrescriptionInstances('patient2', datetime.datetime.now())
        with self.assertRaises(testDatabase.TakePrescription.DoesNotExist):
            testDatabase.TakePrescription.select().where(testDatabase.TakePrescription.currentdate == (datetime.datetime.now().date() + datetime.timedelta(days = 3))).get()

    def testAlreadyCreated(self):
        """Attempt to create another take prescription instances after already created"""
        api.createTakePrescriptionInstances('patient3', datetime.datetime.now())
        self.assertEqual(testDatabase.TakePrescription.select().where(testDatabase.TakePrescription.prescriptionid == 3).count(),1)

    def tearDown(self):
        """Delete all tables"""
        # testDatabase.dropAll()

if __name__ == '__main__':
    unittest.main()