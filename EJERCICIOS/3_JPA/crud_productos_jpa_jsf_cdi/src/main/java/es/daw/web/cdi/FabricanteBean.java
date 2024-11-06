package es.daw.web.cdi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

import es.daw.web.entities.Fabricante;
import es.daw.web.repositories.CrudRepository;

//@Named("fabricanteBean")
//@RequestScoped
@Model
public class FabricanteBean {

    @Inject
    CrudRepository<Fabricante> daoF;

    public List<Fabricante> getFabricantes() {
        return daoF.select();

    }
}
