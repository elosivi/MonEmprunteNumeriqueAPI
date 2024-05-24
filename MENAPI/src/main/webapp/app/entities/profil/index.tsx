import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Profil from './profil';
import ProfilDetail from './profil-detail';
import ProfilUpdate from './profil-update';
import ProfilDeleteDialog from './profil-delete-dialog';

const ProfilRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Profil />} />
    <Route path="new" element={<ProfilUpdate />} />
    <Route path=":id">
      <Route index element={<ProfilDetail />} />
      <Route path="edit" element={<ProfilUpdate />} />
      <Route path="delete" element={<ProfilDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProfilRoutes;
