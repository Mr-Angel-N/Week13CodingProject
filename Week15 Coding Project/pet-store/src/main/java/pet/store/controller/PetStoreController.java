package pet.store.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
  
  @Autowired
  private PetStoreService petStoreService;

  @PostMapping
  public PetStoreData savePetStore(
      @RequestBody PetStoreData petStoreData) {
    log.info("Creating petStoreData{}", petStoreData);
    return petStoreService.savePetStore(petStoreData);
  }
  
  @PutMapping("/{petStoreId}")
  public PetStoreData updatePetStore(@PathVariable Long petStoreId,
      @RequestBody PetStoreData petStoreData) {
  petStoreData.setPetStoreId(petStoreId);
  log.info("Updating petStore {}", petStoreData);
  return petStoreService.savePetStore(petStoreData);
}
  
  @PostMapping("/{petStoreId}/employee")
  @ResponseStatus(HttpStatus.CREATED)
  public PetStoreEmployee addEmployeeToPetStore(@PathVariable Long petStoreId, 
      @RequestBody PetStoreEmployee employee) {
      log.info("Adding employee to pet store with ID: " + petStoreId);
      
      return petStoreService.saveEmployee(petStoreId, employee);
  }
  
  @PostMapping("/{petStoreId}/customer")
  @ResponseStatus(HttpStatus.CREATED)
  public PetStoreCustomer addCustomerToPetStore(@PathVariable Long petStoreId, 
      @RequestBody PetStoreCustomer customer) {
      log.info("Adding customer to pet store with ID: " + petStoreId);
      
      return petStoreService.saveCustomer(petStoreId, customer);
  }
  
  @GetMapping
  public List<PetStoreData> retrieveAllPetStores(){
    log.info("Retrieve all pet stores called");
    return petStoreService.retrieveAllPetStores();
  }
  
  @GetMapping("/{petStoreId}")
  public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId) {
    log.info("Retrieving pet store with ID={}", petStoreId);
    return petStoreService.retrievePetStoreById(petStoreId);
  }
  
  @DeleteMapping("/pet_Store")
  public void deleteAllPetStores() {
    log.info("Attemping to delete all pet stores");
    throw new UnsupportedOperationException(
        "Deleting all pet stores is NOT allowed.");
  }
  
  @DeleteMapping("/{petStoreId}")
  public Map<String, String> deletePetStoreById(
      @PathVariable Long petStoreId)   {
    log.info("Deleting pet store with ID={}", petStoreId);
    
    petStoreService.deletePetStoreById(petStoreId);
    
    return Map.of("message", "Deletion of pet store with ID=" 
    + petStoreId + " was successful.");
  }
}