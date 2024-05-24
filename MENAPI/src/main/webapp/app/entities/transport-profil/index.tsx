import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TransportProfil from './transport-profil';
import TransportProfilDetail from './transport-profil-detail';
import TransportProfilUpdate from './transport-profil-update';
import TransportProfilDeleteDialog from './transport-profil-delete-dialog';

const TransportProfilRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TransportProfil />} />
    <Route path="new" element={<TransportProfilUpdate />} />
    <Route path=":id">
      <Route index element={<TransportProfilDetail />} />
      <Route path="edit" element={<TransportProfilUpdate />} />
      <Route path="delete" element={<TransportProfilDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TransportProfilRoutes;
