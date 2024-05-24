import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Communication from './communication';
import CommunicationDetail from './communication-detail';
import CommunicationUpdate from './communication-update';
import CommunicationDeleteDialog from './communication-delete-dialog';

const CommunicationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Communication />} />
    <Route path="new" element={<CommunicationUpdate />} />
    <Route path=":id">
      <Route index element={<CommunicationDetail />} />
      <Route path="edit" element={<CommunicationUpdate />} />
      <Route path="delete" element={<CommunicationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CommunicationRoutes;
