<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.typePlant.home.createOrEditLabel"
          data-cy="TypePlantCreateUpdateHeading"
          v-text="$t('pfaApp.typePlant.home.createOrEditLabel')"
        >
          Create or edit a TypePlant
        </h2>
        <div>
          <div class="form-group" v-if="typePlant.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="typePlant.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.typePlant.lebelle')" for="type-plant-lebelle">Lebelle</label>
            <input
              type="text"
              class="form-control"
              name="lebelle"
              id="type-plant-lebelle"
              data-cy="lebelle"
              :class="{ valid: !$v.typePlant.lebelle.$invalid, invalid: $v.typePlant.lebelle.$invalid }"
              v-model="$v.typePlant.lebelle.$model"
              required
            />
            <div v-if="$v.typePlant.lebelle.$anyDirty && $v.typePlant.lebelle.$invalid">
              <small class="form-text text-danger" v-if="!$v.typePlant.lebelle.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.typePlant.humiditeMax')" for="type-plant-humiditeMax">Humidite Max</label>
            <input
              type="number"
              class="form-control"
              name="humiditeMax"
              id="type-plant-humiditeMax"
              data-cy="humiditeMax"
              :class="{ valid: !$v.typePlant.humiditeMax.$invalid, invalid: $v.typePlant.humiditeMax.$invalid }"
              v-model.number="$v.typePlant.humiditeMax.$model"
              required
            />
            <div v-if="$v.typePlant.humiditeMax.$anyDirty && $v.typePlant.humiditeMax.$invalid">
              <small class="form-text text-danger" v-if="!$v.typePlant.humiditeMax.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.typePlant.humiditeMax.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.typePlant.humiditeMin')" for="type-plant-humiditeMin">Humidite Min</label>
            <input
              type="number"
              class="form-control"
              name="humiditeMin"
              id="type-plant-humiditeMin"
              data-cy="humiditeMin"
              :class="{ valid: !$v.typePlant.humiditeMin.$invalid, invalid: $v.typePlant.humiditeMin.$invalid }"
              v-model.number="$v.typePlant.humiditeMin.$model"
              required
            />
            <div v-if="$v.typePlant.humiditeMin.$anyDirty && $v.typePlant.humiditeMin.$invalid">
              <small class="form-text text-danger" v-if="!$v.typePlant.humiditeMin.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.typePlant.humiditeMin.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.typePlant.temperature')" for="type-plant-temperature">Temperature</label>
            <input
              type="number"
              class="form-control"
              name="temperature"
              id="type-plant-temperature"
              data-cy="temperature"
              :class="{ valid: !$v.typePlant.temperature.$invalid, invalid: $v.typePlant.temperature.$invalid }"
              v-model.number="$v.typePlant.temperature.$model"
              required
            />
            <div v-if="$v.typePlant.temperature.$anyDirty && $v.typePlant.temperature.$invalid">
              <small class="form-text text-danger" v-if="!$v.typePlant.temperature.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.typePlant.temperature.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.typePlant.luminisite')" for="type-plant-luminisite">Luminisite</label>
            <input
              type="number"
              class="form-control"
              name="luminisite"
              id="type-plant-luminisite"
              data-cy="luminisite"
              :class="{ valid: !$v.typePlant.luminisite.$invalid, invalid: $v.typePlant.luminisite.$invalid }"
              v-model.number="$v.typePlant.luminisite.$model"
              required
            />
            <div v-if="$v.typePlant.luminisite.$anyDirty && $v.typePlant.luminisite.$invalid">
              <small class="form-text text-danger" v-if="!$v.typePlant.luminisite.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.typePlant.luminisite.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.typePlant.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./type-plant-update.component.ts"></script>
