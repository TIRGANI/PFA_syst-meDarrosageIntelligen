/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CapteurUpdateComponent from '@/entities/capteur/capteur-update.vue';
import CapteurClass from '@/entities/capteur/capteur-update.component';
import CapteurService from '@/entities/capteur/capteur.service';

import BracheService from '@/entities/brache/brache.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Capteur Management Update Component', () => {
    let wrapper: Wrapper<CapteurClass>;
    let comp: CapteurClass;
    let capteurServiceStub: SinonStubbedInstance<CapteurService>;

    beforeEach(() => {
      capteurServiceStub = sinon.createStubInstance<CapteurService>(CapteurService);

      wrapper = shallowMount<CapteurClass>(CapteurUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          capteurService: () => capteurServiceStub,
          alertService: () => new AlertService(),

          bracheService: () =>
            sinon.createStubInstance<BracheService>(BracheService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.capteur = entity;
        capteurServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(capteurServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.capteur = entity;
        capteurServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(capteurServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCapteur = { id: 123 };
        capteurServiceStub.find.resolves(foundCapteur);
        capteurServiceStub.retrieve.resolves([foundCapteur]);

        // WHEN
        comp.beforeRouteEnter({ params: { capteurId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.capteur).toBe(foundCapteur);
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
