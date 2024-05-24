import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Prestation from './prestation';
import PrestationDetail from './prestation-detail';
import PrestationUpdate from './prestation-update';
import PrestationDeleteDialog from './prestation-delete-dialog';

const PrestationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Prestation />} />
    <Route path="new" element={<PrestationUpdate />} />
    <Route path=":id">
      <Route index element={<PrestationDetail />} />
      <Route path="edit" element={<PrestationUpdate />} />
      <Route path="delete" element={<PrestationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PrestationRoutes;
