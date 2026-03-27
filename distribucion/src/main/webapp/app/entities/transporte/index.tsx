import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Transporte from './transporte';
import TransporteDetail from './transporte-detail';
import TransporteUpdate from './transporte-update';
import TransporteDeleteDialog from './transporte-delete-dialog';

const TransporteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Transporte />} />
    <Route path="new" element={<TransporteUpdate />} />
    <Route path=":id">
      <Route index element={<TransporteDetail />} />
      <Route path="edit" element={<TransporteUpdate />} />
      <Route path="delete" element={<TransporteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TransporteRoutes;
