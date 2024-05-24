import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Profil from './profil';
import Fonction from './fonction';
import Prestation from './prestation';
import Transport from './transport';
import Materiel from './materiel';
import Communication from './communication';
import Unite from './unite';
import DonneesReferences from './donnees-references';
import PrestationProfil from './prestation-profil';
import TransportProfil from './transport-profil';
import DeplacementProfil from './deplacement-profil';
import MaterielProfil from './materiel-profil';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="profil/*" element={<Profil />} />
        <Route path="fonction/*" element={<Fonction />} />
        <Route path="prestation/*" element={<Prestation />} />
        <Route path="transport/*" element={<Transport />} />
        <Route path="materiel/*" element={<Materiel />} />
        <Route path="communication/*" element={<Communication />} />
        <Route path="unite/*" element={<Unite />} />
        <Route path="donnees-references/*" element={<DonneesReferences />} />
        <Route path="prestation-profil/*" element={<PrestationProfil />} />
        <Route path="transport-profil/*" element={<TransportProfil />} />
        <Route path="deplacement-profil/*" element={<DeplacementProfil />} />
        <Route path="materiel-profil/*" element={<MaterielProfil />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
