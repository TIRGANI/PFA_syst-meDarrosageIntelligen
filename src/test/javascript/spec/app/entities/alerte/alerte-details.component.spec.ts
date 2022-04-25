/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AlerteDetailComponent from '@/entities/alerte/alerte-details.vue';
import AlerteClass from '@/entities/alerte/alerte-details.component';
import AlerteService from '@/entities/alerte/alerte.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Alerte Management Detail Component', () => {
    let wrapper: Wrapper<AlerteClass>;
    let comp: AlerteClass;
    let alerteServiceStub: SinonStubbedInstance<AlerteService>;

    beforeEach(() => {
      alerteServiceStub = sinon.createStubInstance<AlerteService>(AlerteService);

      wrapper = shallowMount<AlerteClass>(AlerteDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { alerteService: () => alerteServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAlerte = { id: 123 };
        alerteServiceStub.find.resolves(foundAlerte);

        // WHEN
        comp.retrieveAlerte(123);
        await comp.$nextTick();

        // THEN
        expect(comp.alerte).toBe(foundAlerte);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAlerte = { id: 123 };
        alerteServiceStub.find.resolves(foundAlerte);

        // WHEN
        comp.beforeRouteEnter({ params: { alerteId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.alerte).toBe(foundAlerte);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
