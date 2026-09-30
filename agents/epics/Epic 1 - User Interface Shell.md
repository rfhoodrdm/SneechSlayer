# Description
- First iteration will be the data loader service, which will support the GUI components.

## Purpose
- This service will access static resources like images and sounds.
    - loaded from the runtime classpath under assets/.
    - Requests use paths relative to that directory.
- Components that need resources will implement the RequiresLoadedData interface
- The List of such components will be injected into the entry class, currently SneechSlayer.
    - Inside of the entry method for this class, we gather the asset requests and submit them to the DataLoader.
    - Once this is done, we return the loaded assets to the component that wants them.
- AssetRequests are given by the AssetRequest class.
- Loaded assets are represented by the Asset interface, which has two implementations: ImageAsset and SoundAsset.
- Assets may have type of SOUND or IMAGE.
- If loading a requested asset fails, at this point in time, the entry class throws a DataLoaderException and exits the program with a system call.
    - We may eventually decide that process may be modified later by instead providing a default asset, or looking in a custom folder for asset overrides first.
- When loading assets, check the image/ sub-folder for IMAGE type asset requests, and sound/ for SOUND type assets 
- Results do not necessarily need to reflect request order. 
- Asset names will include their classification. E.g. background/swamp.gif might be an image located ultimately in assets/image/background/swamp.gif
- For now, every sound asset request produces a new clip. The class that requests the clip is responsible for closing it. 