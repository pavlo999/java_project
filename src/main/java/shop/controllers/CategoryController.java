package shop.controllers;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dto.category.CategoryItemDTO;
import shop.dto.category.CreateCategoryDTO;
import shop.dto.category.UpdateCategoryDTO;
import shop.entities.CategoryEntity;
import shop.interfaces.CategoryService;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService _categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryItemDTO>> index() {
        var result = _categoryService.get();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryItemDTO> create(@Valid @ModelAttribute CreateCategoryDTO model) {
        var result = _categoryService.create(model);
        if(result != null)
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<CategoryItemDTO> update(@Valid @RequestBody UpdateCategoryDTO model) {
        var result = _categoryService.update(model);
        if(result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@PathParam("id") int id) {
        _categoryService.delete(id);
            return new ResponseEntity<>("delete element ", HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryItemDTO> get(@PathVariable("id") int id) {
        var result = _categoryService.get(id);
        if (result!=null)
            return new ResponseEntity<>(result, HttpStatus.OK);

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

}
