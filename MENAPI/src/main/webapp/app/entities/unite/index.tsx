import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Unite from './unite';
import UniteDetail from './unite-detail';
import UniteUpdate from './unite-update';
import UniteDeleteDialog from './unite-delete-dialog';

const UniteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Unite />} />
    <Route path="new" element={<UniteUpdate />} />
    <Route path=":id">
      <Route index element={<UniteDetail />} />
      <Route path="edit" element={<UniteUpdate />} />
      <Route path="delete" element={<UniteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UniteRoutes;
