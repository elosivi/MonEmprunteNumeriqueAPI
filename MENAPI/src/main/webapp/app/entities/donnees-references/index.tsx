import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DonneesReferences from './donnees-references';
import DonneesReferencesDetail from './donnees-references-detail';
import DonneesReferencesUpdate from './donnees-references-update';
import DonneesReferencesDeleteDialog from './donnees-references-delete-dialog';

const DonneesReferencesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DonneesReferences />} />
    <Route path="new" element={<DonneesReferencesUpdate />} />
    <Route path=":id">
      <Route index element={<DonneesReferencesDetail />} />
      <Route path="edit" element={<DonneesReferencesUpdate />} />
      <Route path="delete" element={<DonneesReferencesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DonneesReferencesRoutes;
