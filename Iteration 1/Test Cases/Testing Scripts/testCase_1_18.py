# ORM Testing Script
# Test Case 1_18

from peewee import *
import unittest
import imp

testDatabase = imp.load_source('testDatabase', '../../../Website/testDatabase.py')

class testCase_1_18(unittest.TestCase):

  def setUp(self):
    deleteUsers = testDatabase.Client.delete()
    deletePasswords = testDatabase.uq8LnAWi7D.delete()
    deleteUsers.execute()
    deletePasswords.execute()


  def test_1_18_1(self):
    newUserInsert = testDatabase.Client.insert (
      username= 'test',
      firstName='test',
      surname='test',
      dob='01/01/2001',
      isMale='TRUE',
      isCarer='TRUE',
      email='test@test.com')
    newUserInsert.execute()
    newUserUpdate = testDatabase.Client.update(username='testingUpdate').where(testDatabase.Client.username == 'test')
    newUserUpdate.execute()
    self.assertEqual(testDatabase.Client.select().count(),0)

  def test_1_18_2(self):
    newUserInsert = testDatabase.Client.insert (
      username= 'test',
      firstName='test',
      surname='test',
      dob='01/01/2001',
      isMale='TRUE',
      isCarer='TRUE',
      email='test@test.com')
    newUserInsert.execute()
    newUserUpdate = testDatabase.Client.update(firstname='testingFirstname').where(testDatabase.Client.username == 'test')
    newUserUpdate.execute()
    self.assertEqual(testDatabase.Client.select().count(),0)

  def test_1_18_3(self):
    newUserInsert = testDatabase.Client.insert (
      username= 'test',
      firstName='test',
      surname='test',
      dob='01/01/2001',
      isMale='TRUE',
      isCarer='TRUE',
      email='test@test.com')
    newUserInsert.execute()
    newUserUpdate = testDatabase.Client.update(surename='testingSurename').where(testDatabase.Client.username == 'test')
    newUserUpdate.execute()
    self.assertEqual(testDatabase.Client.select().count(),0)

  def test_1_18_4(self):
    newUserInsert = testDatabase.Client.insert (
      username= 'test',
      firstName='test',
      surname='test',
      dob='01/01/2001',
      isMale='TRUE',
      isCarer='TRUE',
      email='test@test.com')
    newUserInsert.execute()
    newUserUpdate = testDatabase.Client.update(dob='03/03/1993').where(testDatabase.Client.username == 'test')
    newUserUpdate.execute()
    self.assertEqual(testDatabase.Client.select().count(),0)

  def test_1_18_5(self):
    newUserInsert = testDatabase.Client.insert (
      username= 'test',
      firstName='test',
      surname='test',
      dob='01/01/2001',
      isMale='TRUE',
      isCarer='TRUE',
      email='test@test.com')
    newUserInsert.execute()
    newUserUpdate = testDatabase.Client.update(isMale='False').where(testDatabase.Client.username == 'test')
    newUserUpdate.execute()
    self.assertEqual(testDatabase.Client.select().count(),0)

  def test_1_18_6(self):
    newUserInsert = testDatabase.Client.insert (
      username= 'test',
      firstName='test',
      surname='test',
      dob='01/01/2001',
      isMale='TRUE',
      isCarer='TRUE',
      email='test@test.com')
  newUserInsert.execute()
  newUserUpdate = testDatabase.Client.update(isCarer='False').where(testDatabase.Client.username == 'test')
  newUserUpdate.execute()
  self.assertEqual(testDatabase.Client.select().count(),0)

  def test_1_18_7(self):
    newUserInsert = testDatabase.Client.insert (
      username= 'test',
      firstName='test',
      surname='test',
      dob='01/01/2001',
      isMale='TRUE',
      isCarer='TRUE',
      email='test@test.com')
    newUserInsert.execute()
    newUserUpdate = testDatabase.Client.update(email='testingUpdate@testingUpdate.com').where(testDatabase.Client.username == 'test')
    newUserUpdate.execute()
    self.assertEqual(testDatabase.Client.select().count(),0)

  def test_1_18_8(self):
    newUserInsert = Client.insert (
      username= 'test',
      firstName='test',
      surname='test',
      dob='01/01/2001',
      isMale='TRUE',
      isCarer='TRUE',
      email='test@test.com')
    newUserInsert.execute()
    newUserUpdate = testDatabase.Client.update(verified='TRUE').where(testDatabase.Client.username == 'test')
    newUserUpdate.execute()
    self.assertEqual(testDatabase.Client.select().count(),0)

  def test_1_18_9(self):
    newUserInsert = testDatabase.Client.insert (
      username= 'test',
      firstName='test',
      surname='test',
      dob='01/01/2001',
      isMale='TRUE',
      isCarer='TRUE',
      email='test@test.com')
    newUserInsert.execute()
    newUserUpdate = testDatabase.Client.update(accountLocked='TRUE').where(testDatabase.Client.username == 'test')
    newUserUpdate.execute()
    self.assertEqual(testDatabase.Client.select().count(),0)

  def tearDown(self):
    deleteUsers = testDatabase.Client.delete()
    deletePasswords = testDatabase.uq8LnAWi7D.delete()
    deleteUsers.execute()
    deletePasswords.execute()

if __name__ == '__main__':
  unittest.main()
