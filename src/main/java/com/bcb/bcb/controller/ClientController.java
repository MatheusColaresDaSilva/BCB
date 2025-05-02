package com.bcb.bcb.controller;


import com.bcb.bcb.dto.request.ClientRequestDTO;
import com.bcb.bcb.dto.response.ClientResponseDTO;
import com.bcb.bcb.dto.response.ResponseDTO;
import com.bcb.bcb.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController extends BaseController{

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseDTO<ClientResponseDTO>> findById(@PathVariable Long id) {
//        ClientResponseDTO response = clientService.findById(id);
//        return ResponseEntity.ok(new ResponseDTO<>(response));
//    }
//
//    @GetMapping
//    public ResponseEntity<ResponseDTO<List<ClientResponseDTO>>> findAll(Pageable page) {
//        List<ClientResponseDTO> response = clientService.findAll(page);
//        return ResponseEntity.ok(new ResponseDTO<>(response));
//    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ClientResponseDTO>> createClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO response = clientService.createClient(clientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(response));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<ResponseDTO<ClientResponseDTO>> updateClient(@RequestBody ClientRequestDTO clientRequestDTO, @PathVariable Long id) {
//        ClientResponseDTO response = clientService.updateClient(clientRequestDTO, id);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ResponseDTO<>(response));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Long> deleteClient(@PathVariable Long id) {
//        clientService.deleteClient(id);
//        return new ResponseEntity<>(id, HttpStatus.OK);
//    }
}
