package com.example.game.user;

import com.cloudinary.Cloudinary;
import com.example.game.citta.Citta;
import com.example.game.citta.CittaService;
import com.example.game.email.EmailService;
import com.example.game.exceptions.*;
import com.example.game.payloads.entities.UserSignupDTO;
import com.example.game.payloads.entities.UserToPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

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
    EmailService emailService;

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

    public Page<User> findByDateBetween(String date1, String date2, int page, int size, String orderBy, String direction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.ITALIAN);
        LocalDate from = LocalDate.parse(date1, formatter);
        LocalDate to = LocalDate.parse(date2, formatter);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), orderBy));

        return userRepository.findByCreatedAtDateBetween(from, to, pageable);
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
        user.setFullName(user.getNome(), user.getCognome());
        user.setEmail(userSignupDTO.email());
        user.setCitta(cittaService.findById(userSignupDTO.cittaId()));

        return userRepository.save(user);
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
        user.setDeletedAt(LocalDate.now().toString());
        userRepository.save(user);
        return true;
    }

    public User restoreById(long id) {
        User user = findById(id);

        user.setActive(true);
        user.setDeletedAt("");
        return userRepository.save(user);
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

    public boolean askForCode(String email, boolean validation) {
        User user = findByEmail(email);
        String[] strings = new String[]{"a", "A", "b", "B", "c", "C", "d", "D", "e", "E", "f", "F", "g", "G", "h", "H", "i", "I", "l", "L", "m", "M", "n", "N", "o", "O", "p", "P", "q", "Q", "r", "R", "s", "S", "t", "T", "u", "U", "v", "V", "z", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        StringBuilder code = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            Random r = new Random();
            int randInt = r.nextInt(strings.length - 1) + 1;

            code.append(strings[randInt]);
        }

        user.setChangePasswordCode(code.toString());
        userRepository.save(user);

        try {
            if (validation) {
                emailService.sendEmail(user.getEmail(), "Gioco - effettua il primo accesso", "Ciao " + user.getNome() + " " + user.getCognome() + " ti mandiamo questo codice per verificare che sia veramente tu. " + "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        code +
                        "\n" +
                        "\n" +
                        "\n" +
                        " inserisci questo codice nella schermata di login che trovi su Gioco.");
            } else {
                emailService.sendEmail(user.getEmail(), "Codice per cambiare la password", "Ciao!" + user.getNome() + " " + user.getCognome() + " per resettare la tua password inserisci il codice qui sotto " + "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        code + " e la tua mail nel form che vedi sulla schermata di Trasporti e premi invio.");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyChangePasswordCode(String email, String code, boolean validation) {
        User user = findByEmail(email);
        if (null != user.getChangePasswordCode() && user.getChangePasswordCode().equals(code) && validation) {
            user.setValidated(true);
            userRepository.save(user);
            return true;
        }
        ;
        return null != user.getChangePasswordCode() && user.getChangePasswordCode().equals(code);
    }

    public User setProfileImage(User user, MultipartFile multipartFile) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            user.setImmagineProfilo(imageUrl);
            return save(user);
        } catch (Exception e) {
            throw new BadRequestException("Impossibile caricare l'immagine");
        }
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void clearCode(String email) {
        User user = findByEmail(email);
        user.setChangePasswordCode("");
        save(user);
    }

    public User putUser(Long id, Long userAuthId, UserToPatch userToPatch) {
        if (!Objects.equals(id, userAuthId)) throw new UnauthorizedException("Non puoi modificare questo id");
        User user = findById(id);
        user.setNome(userToPatch.nome());
        user.setCognome(userToPatch.cognome());
        user.setCitta(cittaService.findById(userToPatch.cittaId()));
        user.setFullName(user.getNome(), user.getCognome());
        return userRepository.save(user);
    }
}
