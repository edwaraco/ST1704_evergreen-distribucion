import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Separacion from './separacion';
import SeparacionDetail from './separacion-detail';
import SeparacionUpdate from './separacion-update';
import SeparacionDeleteDialog from './separacion-delete-dialog';

const SeparacionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Separacion />} />
    <Route path="new" element={<SeparacionUpdate />} />
    <Route path=":id">
      <Route index element={<SeparacionDetail />} />
      <Route path="edit" element={<SeparacionUpdate />} />
      <Route path="delete" element={<SeparacionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SeparacionRoutes;
