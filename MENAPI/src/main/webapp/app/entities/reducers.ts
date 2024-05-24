import profil from 'app/entities/profil/profil.reducer';
import fonction from 'app/entities/fonction/fonction.reducer';
import prestation from 'app/entities/prestation/prestation.reducer';
import transport from 'app/entities/transport/transport.reducer';
import materiel from 'app/entities/materiel/materiel.reducer';
import communication from 'app/entities/communication/communication.reducer';
import unite from 'app/entities/unite/unite.reducer';
import donneesReferences from 'app/entities/donnees-references/donnees-references.reducer';
import prestationProfil from 'app/entities/prestation-profil/prestation-profil.reducer';
import transportProfil from 'app/entities/transport-profil/transport-profil.reducer';
import deplacementProfil from 'app/entities/deplacement-profil/deplacement-profil.reducer';
import materielProfil from 'app/entities/materiel-profil/materiel-profil.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  profil,
  fonction,
  prestation,
  transport,
  materiel,
  communication,
  unite,
  donneesReferences,
  prestationProfil,
  transportProfil,
  deplacementProfil,
  materielProfil,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
