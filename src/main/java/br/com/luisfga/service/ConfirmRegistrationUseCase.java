package br.com.luisfga.service;

import br.com.luisfga.domain.entities.AppUser;
import br.com.luisfga.service.exceptions.CorruptedLinkageException;
import java.util.Base64;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ConfirmRegistrationUseCase {

    @PersistenceContext(unitName = "applicationJpaUnit")
    private EntityManager em;
    
    public void confirmRegistration(String encodedEmail) throws CorruptedLinkageException {
        
        if (encodedEmail == null || encodedEmail.isEmpty()) {
            throw new CorruptedLinkageException();
        }
        
        try {
            byte[] decodedEmailBytes = Base64.getDecoder().decode(encodedEmail);
            String email = new String(decodedEmailBytes);
            
            Query findByEmail = em.createNamedQuery("AppUser.findByEmail");
            findByEmail.setParameter("email", email);
            AppUser appUser = (AppUser) findByEmail.getSingleResult();
            
            appUser.setStatus("ok");//seta status para OK, i.e. CONFIRMADO
//            em.persist(appUser);
            
        } catch (NoResultException | IllegalArgumentException nrException) {
            throw new CorruptedLinkageException();
            
        }
    }
    
}
