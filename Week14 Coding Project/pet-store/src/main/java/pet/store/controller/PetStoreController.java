package pet.store.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
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

  @GetMapping
  public List<PetStoreData> retrieveAllPetStores(){
    log.info("Retrieve all pet stores called");
    return petStoreService.retrieveAllPetStores();
  }
}