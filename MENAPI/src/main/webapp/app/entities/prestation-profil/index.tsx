import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PrestationProfil from './prestation-profil';
import PrestationProfilDetail from './prestation-profil-detail';
import PrestationProfilUpdate from './prestation-profil-update';
import PrestationProfilDeleteDialog from './prestation-profil-delete-dialog';

const PrestationProfilRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PrestationProfil />} />
    <Route path="new" element={<PrestationProfilUpdate />} />
    <Route path=":id">
      <Route index element={<PrestationProfilDetail />} />
      <Route path="edit" element={<PrestationProfilUpdate />} />
      <Route path="delete" element={<PrestationProfilDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PrestationProfilRoutes;
