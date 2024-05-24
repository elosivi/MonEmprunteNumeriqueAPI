import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Materiel from './materiel';
import MaterielDetail from './materiel-detail';
import MaterielUpdate from './materiel-update';
import MaterielDeleteDialog from './materiel-delete-dialog';

const MaterielRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Materiel />} />
    <Route path="new" element={<MaterielUpdate />} />
    <Route path=":id">
      <Route index element={<MaterielDetail />} />
      <Route path="edit" element={<MaterielUpdate />} />
      <Route path="delete" element={<MaterielDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MaterielRoutes;
