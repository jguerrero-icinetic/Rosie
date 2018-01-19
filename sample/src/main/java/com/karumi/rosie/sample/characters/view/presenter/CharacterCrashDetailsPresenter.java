/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.sample.characters.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.annotation.Success;
import com.karumi.rosie.domain.usecase.callback.OnSuccessCallback;
import com.karumi.rosie.domain.usecase.error.OnErrorCallback;
import com.karumi.rosie.sample.base.view.presenter.MarvelPresenter;
import com.karumi.rosie.sample.characters.domain.model.CharacterCrash;
import com.karumi.rosie.sample.characters.domain.usecase.GetCharacterCrashDetails;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterCrashDetailViewModel;
import com.karumi.rosie.sample.characters.view.viewmodel.mapper.CharacterCrashToCharacterCrashDetailViewModelMapper;

import javax.inject.Inject;

public class CharacterCrashDetailsPresenter
    extends MarvelPresenter<CharacterCrashDetailsPresenter.View> {

  private final GetCharacterCrashDetails getCharacterDetails;
  private final CharacterCrashToCharacterCrashDetailViewModelMapper mapper;
  private String characterKey;

  @Inject public CharacterCrashDetailsPresenter(UseCaseHandler useCaseHandler,
                                                CharacterCrashToCharacterCrashDetailViewModelMapper mapper, GetCharacterCrashDetails getCharacterDetails) {
    super(useCaseHandler);
    this.mapper = mapper;
    this.getCharacterDetails = getCharacterDetails;
  }

  @Override public void update() {
    super.update();
    showLoading();
    loadCharacterDetails();
  }

  public void setCharacterKey(String characterKey) {
    this.characterKey = characterKey;
  }

  private void loadCharacterDetails() {
    getView().hideCharacterCrashDetail();
    createUseCaseCall(getCharacterDetails).args(characterKey).onSuccess(new OnSuccessCallback() {
      @Success public void onCharacterDetailsLoaded(CharacterCrash character) {
        hideLoading();
        CharacterCrashDetailViewModel characterDetailViewModel =
            mapper.mapCharacterToCharacterDetailViewModel(character);
        getView().showCharacterDetail(characterDetailViewModel);
      }
    })
        .onError(new OnErrorCallback() {
          @Override public boolean onError(Error error) {
            getView().hideLoading();
            return false;
          }
        })
        .execute();
  }

  public interface View extends MarvelPresenter.View {
    void hideCharacterCrashDetail();

    void showCharacterDetail(CharacterCrashDetailViewModel character);
  }
}
