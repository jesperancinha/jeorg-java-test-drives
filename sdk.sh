#!/bin/bash

# Install sdkman if not present
if [ ! -d "$HOME/.sdkman" ]; then
    curl -s "https://get.sdkman.io" | bash
fi

source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install latest Java LTS (currently 21 is latest LTS, but project uses 25 which might be a preview or latest)
# According to skill: Choose OpenSDK, then AdaptSDK, then LibreSDK, then Oracle.
# We will use Temurin (Adoptium/AdaptSDK) for LTS 21 or latest available.

sdk install java 21.0.4-tem
sdk use java 21.0.4-tem
