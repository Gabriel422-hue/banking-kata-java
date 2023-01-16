package com.optivem.kata.banking.adapters.driven.fake;

import com.optivem.kata.banking.adapters.driven.ProfileNames;
import com.optivem.kata.banking.core.ports.driven.NationalIdentityProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(ProfileNames.AdapterThirdpartySim)
public class FakeNationalIdentityProviderSimulator implements NationalIdentityProvider {

    private final FakeNationalIdentityProvider provider;

    public FakeNationalIdentityProviderSimulator() {
        this.provider = new FakeNationalIdentityProvider();
        provider.givenExists("SIM_1");
        provider.givenExists("SIM_2");
    }

    @Override
    public boolean exists(String nationalIdentityNumber) {
        return provider.exists(nationalIdentityNumber);
    }
}
