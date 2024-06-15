package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
  
  @Autowired
  private PetStoreDao petStoreDao;
  
  @Autowired
  private EmployeeDao employeeDao;
  
  @Autowired
  private CustomerDao customerDao;
  
  @Transactional(readOnly = false)
  public PetStoreData savePetStore(PetStoreData petStoreData) {
    Long petStoreId = petStoreData.getPetStoreId();
    PetStore petStore = findOrCreatePetStore(petStoreId);
    
    setFieldsInPetStore(petStore, petStoreData);
    return new PetStoreData(petStoreDao.save(petStore));
  }
  
  private void setFieldsInPetStore(PetStore petStore, PetStoreData petStoreData) {
    petStore.setPetStoreName(petStoreData.getPetStoreName());
    petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
    petStore.setPetStoreCity(petStoreData.getPetStoreCity());
    petStore.setPetStorePhone(petStoreData.getPetStorePhone());
    petStore.setPetStoreState(petStoreData.getPetStoreState());
    petStore.setPetStoreZip(petStoreData.getPetStoreZip());
    
  }

  private PetStore findOrCreatePetStore(Long petStoreId) {
    PetStore petStore;
    
    if(Objects.isNull(petStoreId)) {
      petStore = new PetStore();
    }
    else {
      petStore = findPetStoreById(petStoreId);
    }
    
    return petStore;
  }

  private PetStore findPetStoreById(Long petStoreId) {
    
    return petStoreDao.findById(petStoreId)
        .orElseThrow(() -> new NoSuchElementException(
            "PetStore with ID=" + petStoreId + " was not found."));
  }

  @Transactional(readOnly = true)
  public List<PetStoreData> retrieveAllPetStores() {
    List<PetStore> petStores = petStoreDao.findAll();
    List<PetStoreData> response = new LinkedList<>();
    
    for(PetStore petStore : petStores) {
      response.add(new PetStoreData(petStore));
    }
    
    return response;
  }
  
  @Transactional(readOnly = false)
  public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
      PetStore petStore = findPetStoreById(petStoreId);
      Employee employee = findOrCreateEmployee(petStoreEmployee.getEmployeeId(), petStoreId);
      copyEmployeeFields(employee, petStoreEmployee);
      employee.setPetStore(petStore);
      petStore.getEmployees().add(employee);
      employee = employeeDao.save(employee);
      return convertToPetStoreEmployee(employee);
  }
  
  private Employee findEmployeeById(Long petStoreId, Long employeeId) {
    Employee employee = employeeDao.findById(employeeId).orElseThrow(()
        -> new NoSuchElementException("Employee not found"));
    if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
        throw new IllegalArgumentException("Employee does not belong to the specified pet store");
    }
    return employee;
  }

  private Employee findOrCreateEmployee(Long employeeId, Long petStoreId) {
    if (employeeId == null) {
        return new Employee();
    } else {
        return findEmployeeById(petStoreId, employeeId);
    }
  }

  private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
    employee.setEmployeeId(petStoreEmployee.getEmployeeId());
    employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
    employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
    employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
    employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
  }

  private PetStoreEmployee convertToPetStoreEmployee(Employee employee) {
    PetStoreEmployee petStoreEmployee = new PetStoreEmployee();
    petStoreEmployee.setEmployeeId(employee.getEmployeeId());
    petStoreEmployee.setEmployeeFirstName(employee.getEmployeeFirstName());
    petStoreEmployee.setEmployeeLastName(employee.getEmployeeLastName());
    petStoreEmployee.setEmployeePhone(employee.getEmployeePhone());
    petStoreEmployee.setEmployeeJobTitle(employee.getEmployeeJobTitle());
    
    return petStoreEmployee;
  }

  @Transactional(readOnly = false)
  public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
      PetStore petStore = findPetStoreById(petStoreId);
      Customer customer = findOrCreateCustomer(petStoreCustomer.getCustomerId(), petStoreId);
      copyCustomerFields(customer, petStoreCustomer);
      customer.getPetStores().add(petStore);
      petStore.getCustomers().add(customer);
      customer = customerDao.save(customer);
      return convertToPetStoreCustomer(customer);
  }
  
  private Customer findCustomerById(Long petStoreId, Long customerId) {
    Customer customer = customerDao.findById(customerId).orElseThrow(()
        -> new NoSuchElementException("Customer not found"));
    boolean petStoreFound = customer.getPetStores().stream()
        .anyMatch(petStore -> petStore.getPetStoreId().equals(petStoreId));

    if (!petStoreFound) {
      throw new IllegalArgumentException("Customer does not belong to the specified pet store");
    }
    return customer;
  }

  private Customer findOrCreateCustomer(Long customerId, Long petStoreId) {
    if (customerId == null) {
        return new Customer();
    } else {
        return findCustomerById(petStoreId, customerId);
    }
  }

  private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
    customer.setCustomerId(petStoreCustomer.getCustomerId());
    customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
    customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
    customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
  }

  private PetStoreCustomer convertToPetStoreCustomer(Customer customer) {
    PetStoreCustomer petStoreCustomer = new PetStoreCustomer();
    petStoreCustomer.setCustomerId(customer.getCustomerId());
    petStoreCustomer.setCustomerFirstName(customer.getCustomerFirstName());
    petStoreCustomer.setCustomerLastName(customer.getCustomerLastName());
    petStoreCustomer.setCustomerEmail(customer.getCustomerEmail());
    
    return petStoreCustomer;
  }

  public PetStoreData retrievePetStoreById(Long petStoreId) {
    PetStore petStore = findPetStoreById(petStoreId);
    return new PetStoreData(petStore);
  }

  public void deletePetStoreById(Long petStoreId) {
    PetStore petStore = findPetStoreById(petStoreId);
    petStoreDao.delete(petStore);
  }
}