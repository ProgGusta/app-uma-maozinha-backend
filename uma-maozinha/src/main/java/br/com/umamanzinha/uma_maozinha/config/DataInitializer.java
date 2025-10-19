package br.com.umamanzinha.uma_maozinha.config;

import org.springframework.boot.CommandLineRunner;
import br.com.umamanzinha.uma_maozinha.entities.Category;
import br.com.umamanzinha.uma_maozinha.enums.CategoryType;
import br.com.umamanzinha.uma_maozinha.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting category population...");

        for (CategoryType type : CategoryType.values()) {

            if (categoryRepository.findByName(type).isEmpty()) {

                Category category = new Category();
                category.setName(type);
                categoryRepository.save(category);
                System.out.println("Category created: " + type.name());
            }
        }

        System.out.println("Completed category population.");
    }
}
