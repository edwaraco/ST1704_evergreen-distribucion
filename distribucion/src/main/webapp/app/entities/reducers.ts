import cliente from 'app/entities/cliente/cliente.reducer';
import canalComercializacion from 'app/entities/canal-comercializacion/canal-comercializacion.reducer';
import pedido from 'app/entities/pedido/pedido.reducer';
import producto from 'app/entities/producto/producto.reducer';
import transporte from 'app/entities/transporte/transporte.reducer';
import empaque from 'app/entities/empaque/empaque.reducer';
import separacion from 'app/entities/separacion/separacion.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  cliente,
  canalComercializacion,
  pedido,
  producto,
  transporte,
  empaque,
  separacion,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
