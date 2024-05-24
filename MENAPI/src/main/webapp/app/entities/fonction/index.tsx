import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Fonction from './fonction';
import FonctionDetail from './fonction-detail';
import FonctionUpdate from './fonction-update';
import FonctionDeleteDialog from './fonction-delete-dialog';

const FonctionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Fonction />} />
    <Route path="new" element={<FonctionUpdate />} />
    <Route path=":id">
      <Route index element={<FonctionDetail />} />
      <Route path="edit" element={<FonctionUpdate />} />
      <Route path="delete" element={<FonctionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FonctionRoutes;
