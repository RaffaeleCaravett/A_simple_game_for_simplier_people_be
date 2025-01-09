package com.example.game.user;

import com.cloudinary.Cloudinary;
import com.example.game.citta.Citta;
import com.example.game.citta.CittaService;
import com.example.game.exceptions.*;
import com.example.game.payloads.entities.UserSignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CittaService cittaService;
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    PasswordEncoder BCrypt;
    public Page<User> findByParamsAndIsActive(String nome, String cognome, int page, int size, String orderBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), orderBy));
        if (!"".equals(nome) && !"".equals(cognome)) {
            return userRepository.findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCaseAndIsActive(nome, cognome, true, pageable);
        } else if ("".equals(nome) && !"".equals(cognome)) {
            return userRepository.findByCognomeContainingIgnoreCaseAndIsActive(cognome, true, pageable);
        } else if (!"".equals(nome)) {
            return userRepository.findByNomeContainingIgnoreCaseAndIsActive(nome, true, pageable);
        } else {
            throw new InvalidParamsException("I parametri sono vuoti.");
        }
    }

    public List<User> findByDateBetween(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.ITALIAN);
        LocalDate from = LocalDate.parse(date1, formatter);
        LocalDate to = LocalDate.parse(date2, formatter);

        return userRepository.findByCreatedAtDateBetween(from, to);
    }

    public Page<User> findByCitta(long id, int page, int size, String orderBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), orderBy));

        Citta citta = cittaService.findById(id);

        return userRepository.findByCittaAndIsActive(citta, true, pageable);
    }

    public User modifyById(long id, UserSignupDTO userSignupDTO) {
        User user = findById(id);
        if (!user.getEmail().equals(userSignupDTO.email())) {
            if (isEmailUsed(userSignupDTO.email())) {
                throw new EmailAlreadyInUseException(userSignupDTO.email());
            }
        }

        user.setCognome(userSignupDTO.cognome());
        user.setNome(userSignupDTO.nome());
        user.setEmail(userSignupDTO.email());
        user.setCitta(cittaService.findById(userSignupDTO.cittaId()));

        return userRepository.save(user);
    }

    public String updateProfileImage(long id,MultipartFile multipartFile){
        User user = findById(id);

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            user.setImmagineProfilo(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare l'immagine", e);
        }
        userRepository.save(user);
        return user.getImmagineProfilo();
    }

    public User findById(long id) {
        return userRepository.findByIdAndIsActive(id, true).orElseThrow(() -> new UserWithIdNotFoundException(id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailAndIsActive(email, true).orElseThrow(() -> new UserWithEmailNotFoundException(email));
    }

    public boolean isEmailUsed(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean deleteById(long id) {
        User user = findById(id);

        user.setActive(false);
        return true;
    }

    public boolean restoreById(long id) {
        User user = findById(id);

        user.setActive(true);
        return true;
    }

    public boolean permanentlyDeleteUser(long id) {
        User user = findById(id);

        try {
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String askForCode(long id){
        User user = findById(id);
        String[] strings = new String[]{"a","A","b","B","c","C","d","D","e","E","f","F","g","G","h","H","i","I","l","L","m","M","n","N","o","O","p","P","q","Q","r","R","s","S","t","T","u","U","v","V","z","Z","1","2","3","4","5","6","7","8","9"};

        StringBuilder code = new StringBuilder();
        for(int i = 0; i<= 10; i++){
            Random r = new Random();
            int randInt = r.nextInt(strings.length-1) + 1;

            code.append(strings[randInt]);
        }

        user.setChangePasswordCode(code.toString());
        return code.toString();
    }

    public User changePasswordByCode(long id, String code, String newPassword){
        User user = findById(id);

        if(user.getChangePasswordCode().equals(code)){
            user.setPassword(BCrypt.encode(newPassword));
            return userRepository.save(user);
        }else{
            throw new CodeMismatchException(code);
        }
    }
}
