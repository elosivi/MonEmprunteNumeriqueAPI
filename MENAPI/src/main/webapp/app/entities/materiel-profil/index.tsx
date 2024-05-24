import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MaterielProfil from './materiel-profil';
import MaterielProfilDetail from './materiel-profil-detail';
import MaterielProfilUpdate from './materiel-profil-update';
import MaterielProfilDeleteDialog from './materiel-profil-delete-dialog';

const MaterielProfilRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MaterielProfil />} />
    <Route path="new" element={<MaterielProfilUpdate />} />
    <Route path=":id">
      <Route index element={<MaterielProfilDetail />} />
      <Route path="edit" element={<MaterielProfilUpdate />} />
      <Route path="delete" element={<MaterielProfilDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MaterielProfilRoutes;
