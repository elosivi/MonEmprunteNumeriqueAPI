import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DeplacementProfil from './deplacement-profil';
import DeplacementProfilDetail from './deplacement-profil-detail';
import DeplacementProfilUpdate from './deplacement-profil-update';
import DeplacementProfilDeleteDialog from './deplacement-profil-delete-dialog';

const DeplacementProfilRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DeplacementProfil />} />
    <Route path="new" element={<DeplacementProfilUpdate />} />
    <Route path=":id">
      <Route index element={<DeplacementProfilDetail />} />
      <Route path="edit" element={<DeplacementProfilUpdate />} />
      <Route path="delete" element={<DeplacementProfilDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DeplacementProfilRoutes;
